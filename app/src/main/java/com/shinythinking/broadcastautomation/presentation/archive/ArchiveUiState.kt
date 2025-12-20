package com.shinythinking.broadcastautomation.presentation.archive

import com.shinythinking.broadcastautomation.domain.model.Script

sealed interface ArchiveUiState {
    data object Loading : ArchiveUiState
    data class Success(
        val scripts: List<Script>,
        val searchQuery: String
    ) : ArchiveUiState {
        val isEmpty: Boolean get() = scripts.isEmpty()
        val isSearching: Boolean get() = searchQuery.isNotBlank()
    }

    data class Error(val message: String) : ArchiveUiState
}

sealed interface ArchiveEvent {
    data class ShowSnackbar(val message: String) : ArchiveEvent
}
