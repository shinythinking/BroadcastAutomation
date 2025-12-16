package com.solapi.sdk.message.dto.response

import kotlinx.serialization.Serializable

@Serializable
internal data class ErrorResponse(
    val errorCode: String,
    val errorMessage: String
)
