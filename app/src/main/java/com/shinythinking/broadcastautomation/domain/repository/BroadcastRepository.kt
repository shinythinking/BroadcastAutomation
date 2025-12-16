package com.shinythinking.broadcastautomation.domain.repository

import com.shinythinking.broadcastautomation.domain.model.BroadcastMessage

interface BroadcastRepository {
    suspend fun sendBroadcast(message: BroadcastMessage): Result<Unit>
    suspend fun cancelScheduledBroadcast(broadcastId: String)
}