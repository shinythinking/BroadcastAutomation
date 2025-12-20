package com.shinythinking.broadcastautomation.presentation.template_edit

import com.shinythinking.broadcastautomation.domain.model.Template

sealed interface TemplateEditUiState {
    data object Loading : TemplateEditUiState
    data class Success(
        val template: Template,
        val fieldValues: Map<String, String>,
        val generatedScript: String,
        val isGenerating: Boolean = false
    ) : TemplateEditUiState
    data class Error(val message: String) : TemplateEditUiState
}

sealed interface TemplateEditEvent {
    data class ShowSnackbar(val message: String) : TemplateEditEvent
    data class NavigateToScript(val scriptId: String) : TemplateEditEvent
}