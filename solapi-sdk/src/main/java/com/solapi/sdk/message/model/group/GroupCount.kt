package com.solapi.sdk.message.model.group

import kotlinx.serialization.Serializable

@Serializable
data class GroupCount(
    val total: Int? = null,
    val sentTotal: Int? = null,
    val sentFailed: Int? = null,
    val sentSuccess: Int? = null,
    val sentPending: Int? = null,
    val sentReplacement: Int? = null,
    val refund: Int? = null,
    val registeredFailed: Int? = null,
    val registeredSuccess: Int? = null
)
