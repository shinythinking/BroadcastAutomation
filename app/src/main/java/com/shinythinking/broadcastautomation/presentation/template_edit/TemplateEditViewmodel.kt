package com.shinythinking.broadcastautomation.presentation.template_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinythinking.broadcastautomation.domain.model.Script
import com.shinythinking.broadcastautomation.domain.repository.BroadcastRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TemplateEditViewModel @Inject constructor(
    private val repository: BroadcastRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val templateId = checkNotNull(savedStateHandle.get<String>("templateId"))

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
                    _uiState.value = TemplateEditUiState.Error(
                        message = "템플릿을 찾을 수 없습니다"
                    )
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
                    message = "템플릿 로딩 중 오류가 발생했습니다: ${e.message}"
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
            template = currentState.template.template,
            fieldValues = updatedFieldValues
        )

        _uiState.value = currentState.copy(
            fieldValues = updatedFieldValues,
            generatedScript = updatedScript
        )
    }

    private fun generatePreview(template: String, fieldValues: Map<String, String>): String {
        var preview = template
        fieldValues.forEach { (fieldId, value) ->
            preview = preview.replace(
                oldValue = "[$fieldId]",
                newValue = value.ifEmpty { "[$fieldId]" }
            )
        }
        return preview
    }

    fun saveAndContinue() {
        val currentState = _uiState.value
        if (currentState !is TemplateEditUiState.Success) return

        if (!currentState.isValid()) {
            viewModelScope.launch {
                _events.send(
                    TemplateEditEvent.ShowSnackbar("모든 필수 항목을 입력해주세요")
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = currentState.copy(isGenerating = true)

                val script = Script(
                    id = UUID.randomUUID().toString(),
                    title = currentState.template.name,
                    content = currentState.generatedScript,
                    createdAt = LocalDateTime.now(),
                    templateId = templateId,
                )

                repository.saveScript(script)

                _events.send(TemplateEditEvent.NavigateToScript(script.id))

            } catch (e: Exception) {
                _uiState.value = currentState.copy(isGenerating = false)

                _events.send(
                    TemplateEditEvent.ShowSnackbar(
                        "대본 저장 중 오류가 발생했습니다: ${e.message}"
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