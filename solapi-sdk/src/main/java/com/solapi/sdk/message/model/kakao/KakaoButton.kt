package com.solapi.sdk.message.model.kakao

import kotlinx.serialization.Serializable

@Serializable
data class KakaoButton(
    var buttonName: String,
    var buttonType: KakaoButtonType,
    var linkMo: String? = null,
    var linkPc: String? = null,
    var linkAnd: String? = null,
    var linkIos: String? = null,
)