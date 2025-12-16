package com.solapi.sdk.message.model.group

import kotlinx.serialization.Serializable

@Serializable
data class AppInfo(
    val profit: AppProfit? = null,
    val app: String? = null,
    val version: String? = null
)
