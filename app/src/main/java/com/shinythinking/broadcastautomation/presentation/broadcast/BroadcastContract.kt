package com.shinythinking.broadcastautomation.presentation.broadcast

import com.shinythinking.broadcastautomation.domain.model.Script
import java.time.LocalDateTime

sealed interface BroadcastUiState {
    data object Loading : BroadcastUiState

    data class Ready(
        val script: Script,
        val generatedAt: LocalDateTime,
        val estimatedDuration: Int,
        val isBroadcasting: Boolean = false
    ) : BroadcastUiState

    data class Error(
        val message: String,
        val script: Script? = null
    ) : BroadcastUiState
}

sealed interface BroadcastEvent {
    data class ShowSnackbar(val message: String) : BroadcastEvent
    data object NavigateToMain : BroadcastEvent
    data class NavigateToSchedule(val scriptId: String) : BroadcastEvent
}