package com.solapi.sdk.message.model.rcs

import kotlinx.serialization.Serializable

@Serializable
data class RcsOption(
    var brandId: String? = null,
    var mmsType: String? = null,
    var templateId: String? = null,
    var variables: MutableMap<String, String> = mutableMapOf()
)