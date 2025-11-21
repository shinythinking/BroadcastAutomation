package com.shinythinking.broadcastautomation.presentation.script_edit

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
class ScriptEditViewmodel @Inject constructor(
    private val repository: BroadcastRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val scriptId = savedStateHandle.get<String>("scriptId")

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
                if (scriptId != null) {
                    val script = repository.getScript(scriptId)
                    if (script == null) {
                        _uiState.value = ScriptEditUiState.Error("대본을 찾을 수 없습니다")
                    } else {
                        _uiState.value = ScriptEditUiState.Success(
                            script = script,
                            content = script.content
                        )
                    }
                } else {
                    _uiState.value = ScriptEditUiState.Success(
                        script = null,
                        content = ""
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
        if (currentState is ScriptEditUiState.Success) {
            _uiState.value = currentState.copy(content = content)
        }
    }

    fun saveAndContinue() {
        val currentState = _uiState.value
        if (currentState !is ScriptEditUiState.Success) return

        if (!currentState.isValid) {
            viewModelScope.launch {
                _events.send(ScriptEditEvent.ShowSnackbar("대본 내용을 입력해주세요"))
            }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = currentState.copy(isSaving = true)

                val scriptToSave = if (currentState.script != null) {
                    currentState.script.copy(content = currentState.content)
                } else {
                    Script(
                        id = UUID.randomUUID().toString(),
                        title = "새 방송",
                        content = currentState.content,
                        createdAt = LocalDateTime.now(),
                        templateId = "" //todo 가져와야 함
                    )
                }

                repository.saveScript(scriptToSave)
                _events.send(ScriptEditEvent.NavigateToTTS(scriptToSave.id))

            } catch (e: Exception) {
                _uiState.value = currentState.copy(isSaving = false)
                _events.send(
                    ScriptEditEvent.ShowSnackbar(
                        "대본 저장 중 오류가 발생했습니다: ${e.message}"
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