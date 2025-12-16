package com.solapi.sdk.message.dto.response

import com.solapi.sdk.message.model.MessageType
import kotlinx.serialization.Serializable

@Serializable
data class SingleMessageSentResponse(
    val groupId: String,
    val to: String,
    val from: String,
    val type: MessageType,
    val statusMessage: String,
    val country: String,
    val messageId: String,
    val statusCode: String,
    val accountId: String
)
