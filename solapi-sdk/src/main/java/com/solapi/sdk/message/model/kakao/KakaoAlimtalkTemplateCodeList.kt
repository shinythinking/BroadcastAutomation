package com.solapi.sdk.message.model.kakao

import kotlinx.serialization.Serializable

@Serializable
data class KakaoAlimtalkTemplateCodeList(
    var code: String? = null,
    var service: String? = null,
    var isMain: Boolean? = null,
    var status: KakaoAlimtalkTemplateStatus? = null,
    var dormant: Boolean? = null,
    var outDated: Boolean? = null,
    var comments: List<Comment>? = null
) {
    @Serializable
    data class Comment(
        var memberId: String? = null,
        var isAdmin: Boolean? = null,
        var content: String? = null,
        var dateCreated: String? = null
    )
}
