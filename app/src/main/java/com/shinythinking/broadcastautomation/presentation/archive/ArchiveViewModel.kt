package com.shinythinking.broadcastautomation.presentation.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinythinking.broadcastautomation.domain.repository.LocalDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchiveViewModel @Inject constructor(
    private val repository: LocalDataRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ArchiveUiState> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                repository.getScriptsFlow()
            } else {
                repository.searchScripts(query)
            }
        }
        .map { scripts ->
            ArchiveUiState.Success(
                scripts = scripts,
                searchQuery = _searchQuery.value
            ) as ArchiveUiState
        }
        .catch { e ->
            emit(ArchiveUiState.Error(e.message ?: "방송 목록을 불러올 수 없습니다"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ArchiveUiState.Loading
        )

    private val _events = Channel<ArchiveEvent>(Channel.BUFFERED)
    val events: Flow<ArchiveEvent> = _events.receiveAsFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun deleteScript(scriptId: String) {
        viewModelScope.launch {
            try {
                repository.deleteScript(scriptId)
                _events.send(ArchiveEvent.ShowSnackbar("방송이 삭제되었습니다"))
            } catch (e: Exception) {
                _events.send(
                    ArchiveEvent.ShowSnackbar(
                        "방송 삭제 중 오류가 발생했습니다: ${e.message}"
                    )
                )
            }
        }
    }
}

// 나중에 추가 즐겨찾기
//fun toggleFavorite(scriptId: String, currentStatus: Boolean) {
//    viewModelScope.launch {
//        try {
//            val newStatus = !currentStatus
//            repository.updateFavoriteStatus(scriptId, newStatus)
//            val message = if (newStatus) {
//                "즐겨찾기에 추가되었습니다"
//            } else {
//                "즐겨찾기에서 제거되었습니다"
//            }
//            _events.send(ArchiveEvent.ShowSnackbar(message))
//        } catch (e: Exception) {
//            _events.send(
//                ArchiveEvent.ShowSnackbar(
//                    "즐겨찾기 업데이트 중 오류가 발생했습니다: ${e.message}"
//                )
//            )
//        }
//    }
//}