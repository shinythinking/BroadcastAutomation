package com.shinythinking.broadcastautomation.presentation.template_edit

import com.shinythinking.broadcastautomation.domain.model.Template

sealed interface TemplateEditUiState {
    data object Loading : TemplateEditUiState

    data class Success(
        val template: Template,
        val fieldValues: Map<String, String>,
        val generatedScript: String,
        val isGenerating: Boolean = false
    ) : TemplateEditUiState {
        fun isValid(): Boolean {
            return template.fields
                .filter { it.placeholder.isEmpty() }
                .all { field ->
                    val value = fieldValues[field.id]
                    !value.isNullOrBlank()
                }
        }

        fun hasEmptyFields(): Boolean {
            return generatedScript.contains("[") && generatedScript.contains("]")
        }
    }

    data class Error(
        val message: String
    ) : TemplateEditUiState
}

sealed interface TemplateEditEvent {
    data class ShowSnackbar(val message: String) : TemplateEditEvent
    data class NavigateToScript(val scriptId: String) : TemplateEditEvent
}