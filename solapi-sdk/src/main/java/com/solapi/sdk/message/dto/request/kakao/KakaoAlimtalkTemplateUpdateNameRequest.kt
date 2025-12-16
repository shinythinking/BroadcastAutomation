package com.solapi.sdk.message.dto.request.kakao

import kotlinx.serialization.Serializable

@Serializable
data class KakaoAlimtalkTemplateUpdateNameRequest(var name: String? = null)
