package com.shinythinking.broadcastautomation.presentation.ai

import com.shinythinking.broadcastautomation.domain.model.AIGenerationOptions

sealed interface AIGenerationUiState {
    data class Editing(
        val keywords: String,
        val options: AIGenerationOptions
    ) : AIGenerationUiState

    data class Generating(
        val keywords: String,
        val options: AIGenerationOptions
    ) : AIGenerationUiState
}

sealed interface AIGenerationEvent {
    data class ShowSnackbar(val message: String) : AIGenerationEvent
    data class NavigateToScript(val scriptId: String) : AIGenerationEvent
}