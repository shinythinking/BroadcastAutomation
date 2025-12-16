package com.shinythinking.broadcastautomation.data.repository

import com.shinythinking.broadcastautomation.data.mapper.toDto
import com.shinythinking.broadcastautomation.data.remote.SolapiDataSource
import com.shinythinking.broadcastautomation.domain.model.BroadcastMessage
import com.shinythinking.broadcastautomation.domain.repository.BroadcastRepository
import javax.inject.Inject

class BroadcastRepositoryImpl @Inject constructor(
    private val solapiDataSource: SolapiDataSource
) : BroadcastRepository {
    override suspend fun sendBroadcast(message: BroadcastMessage): Result<Unit> {
        val messageDto = message.toDto()

        return solapiDataSource.sendMessage(messageDto)
    }

    override suspend fun cancelScheduledBroadcast(broadcastId: String) {
        TODO("Not yet implemented")
    }

}