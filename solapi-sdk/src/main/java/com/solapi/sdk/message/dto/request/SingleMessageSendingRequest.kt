package com.solapi.sdk.message.dto.request

import com.solapi.sdk.message.model.Message
import kotlinx.serialization.Serializable

@Serializable
data class SingleMessageSendingRequest(
    val message: Message,
) : AbstractDefaultMessageRequest()
