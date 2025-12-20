package com.shinythinking.broadcastautomation.presentation.template_list

import com.shinythinking.broadcastautomation.domain.model.Template

sealed interface TemplateListUiState {
    data object Loading : TemplateListUiState
    data class Success(val templates: List<Template>) : TemplateListUiState {
        val isEmpty: Boolean get() = templates.isEmpty()
    }

    data class Error(val message: String) : TemplateListUiState
}

sealed interface TemplateListEvent {
    data class ShowSnackbar(val message: String) : TemplateListEvent
}