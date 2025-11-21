package com.shinythinking.broadcastautomation.presentation.script_edit

import com.shinythinking.broadcastautomation.domain.model.Script

sealed interface ScriptEditUiState {
    data object Loading : ScriptEditUiState

    data class Success(
        val script: Script?,
        val content: String,
        val isSaving: Boolean = false
    ) : ScriptEditUiState {
        val isValid: Boolean get() = content.isNotBlank()
        val isNewScript: Boolean get() = script == null
    }

    data class Error(val message: String) : ScriptEditUiState
}

sealed interface ScriptEditEvent {
    data class ShowSnackbar(val message: String) : ScriptEditEvent
    data class NavigateToTTS(val scriptId: String) : ScriptEditEvent
}
