package com.solapi.sdk.message.dto.response

import com.solapi.sdk.message.dto.response.common.CommonListResponse
import com.solapi.sdk.message.model.Message
import kotlinx.serialization.Serializable

@Serializable
data class MessageListResponse(
    var messageList: Map<String, Message>? = null
) : CommonListResponse()
