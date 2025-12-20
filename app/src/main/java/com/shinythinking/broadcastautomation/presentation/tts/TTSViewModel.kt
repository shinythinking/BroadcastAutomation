package com.shinythinking.broadcastautomation.presentation.tts

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.shinythinking.broadcastautomation.domain.model.VoiceGender
import com.shinythinking.broadcastautomation.domain.model.VoiceSettings
import com.shinythinking.broadcastautomation.domain.model.VoiceSpeed
import com.shinythinking.broadcastautomation.domain.model.VoiceVolume
import com.shinythinking.broadcastautomation.domain.usecase.GenerateVoiceUseCase
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
class TTSViewModel @Inject constructor(
    private val generateVoiceUseCase: GenerateVoiceUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args = savedStateHandle.toRoute<Screen.TTSSettings>()
    private val scriptId: String = args.scriptId

    private val _uiState = MutableStateFlow<TTSUiState>(
        TTSUiState.Configuring(VoiceSettings())
    )
    val uiState: StateFlow<TTSUiState> = _uiState.asStateFlow()

    private val _events = Channel<TTSEvent>(Channel.BUFFERED)
    val events: Flow<TTSEvent> = _events.receiveAsFlow()

    fun updateGender(gender: VoiceGender) {
        val currentState = _uiState.value
        if (currentState is TTSUiState.Configuring) {
            _uiState.value = currentState.copy(
                settings = currentState.settings.copy(voiceGender = gender)
            )
        }
    }

    fun updateVolume(volume: VoiceVolume) {
        val currentState = _uiState.value
        if (currentState is TTSUiState.Configuring) {
            _uiState.value = currentState.copy(
                settings = currentState.settings.copy(volume = volume)
            )
        }
    }

    fun updateSpeed(speed: VoiceSpeed) {
        val currentState = _uiState.value
        if (currentState is TTSUiState.Configuring) {
            _uiState.value = currentState.copy(
                settings = currentState.settings.copy(speed = speed)
            )
        }
    }

    fun generateVoice() {
        val currentState = _uiState.value
        if (currentState !is TTSUiState.Configuring) return

        viewModelScope.launch {
            _uiState.value = TTSUiState.Generating(currentState.settings)

            generateVoiceUseCase(scriptId, currentState.settings)
                .onSuccess {
                    _events.send(TTSEvent.NavigateToPreview(scriptId))
                }
                .onFailure { e ->
                    _uiState.value = TTSUiState.Configuring(currentState.settings)
                    _events.send(
                        TTSEvent.ShowSnackbar(
                            "음성 생성 중 오류가 발생했습니다: ${e.message}"
                        )
                    )
                }
        }
    }
}