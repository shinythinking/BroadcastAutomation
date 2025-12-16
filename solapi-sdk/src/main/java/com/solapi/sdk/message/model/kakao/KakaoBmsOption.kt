package com.solapi.sdk.message.model.kakao

import kotlinx.serialization.Serializable

@Serializable
data class KakaoBmsOption(
    var targeting: KakaoBmsTargeting? = null
)

