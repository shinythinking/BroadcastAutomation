package com.solapi.sdk.message.model.group

import kotlinx.serialization.Serializable

@Serializable
data class GroupPoint(
    val requested: Float? = null,
    val replacement: Float? = null,
    val additional: Float? = null,
    val refund: Float? = null,
    val sum: Float? = null
)
