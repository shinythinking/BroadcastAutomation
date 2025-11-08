package com.shinythinking.broadcastautomation.presentation.template

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinythinking.broadcastautomation.domain.model.Template
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplateViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow<TemplateUiState>(TemplateUiState.Loading)
    val uiState: StateFlow<TemplateUiState> = _uiState.asStateFlow()

    init {
        loadTemplates()
    }

    private fun loadTemplates() {
        viewModelScope.launch {

        }
    }

    fun getTemplate(templateId: String): Template? {
        return (_uiState.value as? TemplateUiState.Success)?.templates?.find { it.id == templateId }
    }
}