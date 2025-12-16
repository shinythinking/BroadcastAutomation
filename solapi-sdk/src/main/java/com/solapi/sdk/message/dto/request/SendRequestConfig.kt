package com.solapi.sdk.message.dto.request

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class SendRequestConfig(
    var appId: String? = null,
    var allowDuplicates: Boolean = false,
    var showMessageList: Boolean = false,
    @Contextual
    var scheduledDate: Instant? = null
)
