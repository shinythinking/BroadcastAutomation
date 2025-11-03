package com.shinythinking.broadcastautomation.presentation.template

import com.shinythinking.broadcastautomation.domain.model.Template

sealed interface TemplateUiState {
    data object Loading : TemplateUiState
    data class Success(val templates: List<Template>) : TemplateUiState
    data class Error(val message: String) : TemplateUiState
}