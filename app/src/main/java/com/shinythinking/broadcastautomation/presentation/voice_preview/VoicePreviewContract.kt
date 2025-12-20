package com.shinythinking.broadcastautomation.presentation.voice_preview

sealed interface VoicePreviewUiState {
    data class Idle(val progress: Float = 0f) : VoicePreviewUiState
    data class Playing(val progress: Float) : VoicePreviewUiState
    data class Paused(val progress: Float) : VoicePreviewUiState
    data class Completed(val duration: Float = 1f) : VoicePreviewUiState
}