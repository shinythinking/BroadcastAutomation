package com.solapi.sdk.message.dto.response.common

import kotlinx.serialization.Serializable

@Serializable
abstract class CommonListResponse(
    var limit: Int? = null,
    var startKey: String? = null,
    var nextKey: String? = null,
)