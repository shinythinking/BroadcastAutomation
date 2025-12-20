package com.shinythinking.broadcastautomation.presentation.template_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.shinythinking.broadcastautomation.domain.repository.LocalDataRepository
import com.shinythinking.broadcastautomation.domain.usecase.GenerateScriptFromTemplateUseCase
import com.shinythinking.broadcastautomation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplateEditViewModel @Inject constructor(
    private val repository: LocalDataRepository,
    private val generateScriptUseCase: GenerateScriptFromTemplateUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val args = savedStateHandle.toRoute<Screen.TemplateEdit>()
    private val templateId: String = args.templateId

    private val _uiState = MutableStateFlow<TemplateEditUiState>(TemplateEditUiState.Loading)
    val uiState: StateFlow<TemplateEditUiState> = _uiState.asStateFlow()

    private val _events = Channel<TemplateEditEvent>(Channel.BUFFERED)
    val events: Flow<TemplateEditEvent> = _events.receiveAsFlow()

    init {
        loadTemplate()
    }

    private fun loadTemplate() {
        viewModelScope.launch {
            try {
                val template = repository.getTemplate(templateId)

                if (template == null) {
                    _uiState.value = TemplateEditUiState.Error("템플릿을 찾을 수 없습니다")
                    return@launch
                }

                val initialFieldValues = template.fields.associate { field ->
                    field.id to field.placeholder
                }

                val initialScript = generatePreview(template.template, initialFieldValues)

                _uiState.value = TemplateEditUiState.Success(
                    template = template,
                    fieldValues = initialFieldValues,
                    generatedScript = initialScript
                )
            } catch (e: Exception) {
                _uiState.value = TemplateEditUiState.Error(
                    "템플릿 로딩 중 오류가 발생했습니다: ${e.message}"
                )
            }
        }
    }

    fun updateFieldValue(fieldId: String, value: String) {
        val currentState = _uiState.value
        if (currentState !is TemplateEditUiState.Success) return

        val updatedFieldValues = currentState.fieldValues.toMutableMap().apply {
            put(fieldId, value)
        }

        val updatedScript = generatePreview(
            currentState.template.template,
            updatedFieldValues
        )

        _uiState.value = currentState.copy(
            fieldValues = updatedFieldValues,
            generatedScript = updatedScript
        )
    }
    // Refactor
//    private fun formatDateToKorean(dateString: String): String {
//        if (!dateString.matches(Regex("""\d{4}-\d{2}-\d{2}"""))) {
//            return dateString
//        }
//        val parts = dateString.split("-")
//        val month = parts[1].toInt()
//        val day = parts[2].toInt()
//        return "${month}월 ${day}일"
//    }
//
//    private fun formatTimeToKorean(timeString: String): String {
//        if (!timeString.matches(Regex("""\d{2}:\d{2}"""))) {
//            return timeString
//        }
//        val parts = timeString.split(":")
//        val hour = parts[0].toInt()
//        val minute = parts[1].toInt()
//
//        if (minute == 0) {
//            return "${hour}시"
//        }
//        return "${hour}시 ${minute}분"
//    }


    private fun generatePreview(template: String, fieldValues: Map<String, String>): String {
        var preview = template
        fieldValues.forEach { (fieldId, value) ->
            preview = preview.replace("[$fieldId]", value.ifEmpty { "[$fieldId]" })
        }
        return preview
    }

    fun saveAndContinue() {
        val currentState = _uiState.value
        if (currentState !is TemplateEditUiState.Success) return

        viewModelScope.launch {
            _uiState.value = currentState.copy(isGenerating = true)

            generateScriptUseCase(currentState.template, currentState.fieldValues)
                .onSuccess { script ->
                    _events.send(TemplateEditEvent.NavigateToScript(script.id))
                }
                .onFailure { e ->
                    _uiState.value = currentState.copy(isGenerating = false)
                    _events.send(
                        TemplateEditEvent.ShowSnackbar(
                            "대본 생성 중 오류가 발생했습니다: ${e.message}"
                        )
                    )
                }
        }
    }

    fun retry() {
        _uiState.value = TemplateEditUiState.Loading
        loadTemplate()
    }
}