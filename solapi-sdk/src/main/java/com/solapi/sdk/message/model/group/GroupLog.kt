package com.solapi.sdk.message.model.group

import kotlinx.serialization.Serializable

@Serializable
data class GroupLog(
    val createAt: String? = null, // Assuming date is sent as string
    val message: String? = null,
    val oldBalance: Float? = null,
    val newBalance: Float? = null,
    val oldPoint: Float? = null,
    val newPoint: Float? = null,
    val totalPrice: Float? = null
)
