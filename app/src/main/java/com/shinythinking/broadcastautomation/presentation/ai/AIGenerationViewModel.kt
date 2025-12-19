package com.shinythinking.broadcastautomation.presentation.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinythinking.broadcastautomation.domain.model.AIGenerationOptions
import com.shinythinking.broadcastautomation.domain.model.AILength
import com.shinythinking.broadcastautomation.domain.model.AITone
import com.shinythinking.broadcastautomation.domain.usecase.GenerateScriptFromAIUseCase
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
class AIGenerationViewModel @Inject constructor(
    private val generateScriptUseCase: GenerateScriptFromAIUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AIGenerationUiState>(
        AIGenerationUiState.Editing(
            keywords = "",
            options = AIGenerationOptions()
        )
    )
    val uiState: StateFlow<AIGenerationUiState> = _uiState.asStateFlow()

    private val _events = Channel<AIGenerationEvent>(Channel.BUFFERED)
    val events: Flow<AIGenerationEvent> = _events.receiveAsFlow()

    fun updateKeywords(keywords: String) {
        val currentState = _uiState.value
        if (currentState is AIGenerationUiState.Editing) {
            _uiState.value = currentState.copy(keywords = keywords)
        }
    }

    fun updateTone(tone: AITone) {
        val currentState = _uiState.value
        if (currentState is AIGenerationUiState.Editing) {
            _uiState.value = currentState.copy(
                options = currentState.options.copy(tone = tone)
            )
        }
    }

    fun updateLength(length: AILength) {
        val currentState = _uiState.value
        if (currentState is AIGenerationUiState.Editing) {
            _uiState.value = currentState.copy(
                options = currentState.options.copy(length = length)
            )
        }
    }

    fun generateScript() {
        val currentState = _uiState.value
        if (currentState !is AIGenerationUiState.Editing) return

        viewModelScope.launch {
            _uiState.value = AIGenerationUiState.Generating(
                keywords = currentState.keywords,
                options = currentState.options
            )

            generateScriptUseCase(currentState.keywords, currentState.options)
                .onSuccess { script ->
                    _events.send(AIGenerationEvent.NavigateToScript(script.id))
                }
                .onFailure { e ->
                    _uiState.value = AIGenerationUiState.Editing(
                        keywords = currentState.keywords,
                        options = currentState.options
                    )
                    _events.send(
                        AIGenerationEvent.ShowSnackbar(
                            e.message ?: "AI 대본 생성 중 오류가 발생했습니다"
                        )
                    )
                }
        }
    }
}