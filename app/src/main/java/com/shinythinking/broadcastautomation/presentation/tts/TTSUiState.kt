package com.shinythinking.broadcastautomation.presentation.tts

import com.shinythinking.broadcastautomation.domain.model.VoiceSettings

sealed interface TTSUiState {
    data class Configuring(val settings: VoiceSettings) : TTSUiState
    data class Generating(val settings: VoiceSettings) : TTSUiState
}

sealed interface TTSEvent {
    data class ShowSnackbar(val message: String) : TTSEvent
    data class NavigateToPreview(val scriptId: String) : TTSEvent
}