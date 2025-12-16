package com.shinythinking.broadcastautomation.domain.model

sealed class BroadcastSchedule {
    data object Immediate : BroadcastSchedule()
    data class Scheduled(val scheduledTime: String) : BroadcastSchedule()
}