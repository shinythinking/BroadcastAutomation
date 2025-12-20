package com.shinythinking.broadcastautomation.presentation.script_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.shinythinking.broadcastautomation.domain.repository.LocalDataRepository
import com.shinythinking.broadcastautomation.domain.usecase.GenerateScriptFromNoneUseCase
import com.shinythinking.broadcastautomation.domain.usecase.NoneTemplate
import com.shinythinking.broadcastautomation.domain.usecase.UpdateScriptUseCase
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
class ScriptEditViewModel @Inject constructor(
    private val repository: LocalDataRepository,
    private val updateScriptUseCase: UpdateScriptUseCase,
    private val generateScriptFromNoneUseCase: GenerateScriptFromNoneUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val args = savedStateHandle.toRoute<Screen.ScriptEdit>()
    private val scriptId: String = args.scriptId

    private val _uiState = MutableStateFlow<ScriptEditUiState>(ScriptEditUiState.Loading)
    val uiState: StateFlow<ScriptEditUiState> = _uiState.asStateFlow()

    private val _events = Channel<ScriptEditEvent>(Channel.BUFFERED)
    val events: Flow<ScriptEditEvent> = _events.receiveAsFlow()

    init {
        loadScript()
    }

    private fun loadScript() {
        viewModelScope.launch {
            try {
                val script = repository.getScript(scriptId)
                if (script == null) {
                    val noneTemplate = NoneTemplate(
                        title = "새 대본",
                        content = ""
                    )
                    generateScriptFromNoneUseCase(noneTemplate)
                        .onSuccess {
                            _uiState.value = ScriptEditUiState.Editing(
                                script = it,
                            )
                        }
                        .onFailure {
                            _uiState.value =
                                ScriptEditUiState.Error(it.message ?: "대본 생성 중 오류가 발생했습니다")
                        }
                } else {
                    _uiState.value = ScriptEditUiState.Editing(
                        script = script,
                    )
                }
            } catch (e: Exception) {
                _uiState.value = ScriptEditUiState.Error(
                    "대본을 불러오는 중 오류가 발생했습니다: ${e.message}"
                )
            }
        }
    }

    fun updateContent(content: String) {
        val currentState = _uiState.value
        if (currentState is ScriptEditUiState.Editing) {
            val updatedScript = currentState.script.copy(content = content)
            _uiState.value = currentState.copy(script = updatedScript)
        }
    }

    fun saveAndContinue() {
        val currentState = _uiState.value
        if (currentState !is ScriptEditUiState.Editing) return

        viewModelScope.launch {
            _uiState.value = currentState.copy(isSaving = true)


            val updated = currentState.script.copy(content = currentState.content)

            val result = updateScriptUseCase(updated)

            result
                .onSuccess { scriptId ->
                    _events.send(ScriptEditEvent.NavigateToTTS(scriptId))
                }
                .onFailure { e ->
                    _uiState.value = currentState.copy(isSaving = false)
                    _events.send(
                        ScriptEditEvent.ShowSnackbar(
                            e.message ?: "대본 저장 중 오류가 발생했습니다"
                        )
                    )
                }
        }
    }

    fun retry() {
        _uiState.value = ScriptEditUiState.Loading
        loadScript()
    }
}