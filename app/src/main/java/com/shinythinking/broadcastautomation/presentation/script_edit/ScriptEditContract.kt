package com.shinythinking.broadcastautomation.presentation.script_edit

import com.shinythinking.broadcastautomation.domain.model.Script

sealed interface ScriptEditUiState {
    data object Loading : ScriptEditUiState
    data class Editing(
        val script: Script,
        val isSaving: Boolean = false
    ) : ScriptEditUiState {
        val isValid: Boolean get() = content.isNotBlank()
        val content: String get() = script.content
    }
    data class Error(val message: String) : ScriptEditUiState
}

sealed interface ScriptEditEvent {
    data class ShowSnackbar(val message: String) : ScriptEditEvent
    data class NavigateToTTS(val scriptId: String) : ScriptEditEvent
}
