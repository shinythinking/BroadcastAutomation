package com.shinythinking.broadcastautomation.presentation.template_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinythinking.broadcastautomation.domain.model.Template
import com.shinythinking.broadcastautomation.domain.repository.LocalDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TemplateListViewModel @Inject constructor(
    repository: LocalDataRepository
) : ViewModel() {
    val uiState: StateFlow<TemplateListUiState> = repository.getTemplatesFlow()
        .map<List<Template>, TemplateListUiState> { templates ->
            TemplateListUiState.Success(templates)
        }
        .catch { e ->
            emit(TemplateListUiState.Error(e.message ?: "템플릿을 불러올 수 없습니다"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TemplateListUiState.Loading
        )

    private val _events = Channel<TemplateListEvent>(Channel.BUFFERED)
    val events: Flow<TemplateListEvent> = _events.receiveAsFlow()
}