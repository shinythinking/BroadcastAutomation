package com.solapi.sdk.message.dto.request

import com.solapi.sdk.message.model.Message
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class MultipleDetailMessageSendingRequest(
    var messages: List<Message> = emptyList(),
    @Contextual
    var scheduledDate: Instant? = null,
    var showMessageList: Boolean = false,
) : AbstractDefaultMessageRequest()
