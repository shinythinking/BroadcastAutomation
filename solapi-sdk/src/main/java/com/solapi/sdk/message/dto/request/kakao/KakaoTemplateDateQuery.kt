package com.solapi.sdk.message.dto.request.kakao

import java.time.Instant

data class KakaoTemplateDateQuery(
    val date: Instant,
    val queryCondition: KakaoAlimtalkTemplateDateQueryCondition,
) {
    enum class KakaoAlimtalkTemplateDateQueryCondition {
        EQUALS,
        GREATER_THEN_OR_EQUAL,
        GREATER_THEN,
        LESS_THEN_OR_EQUAL,
        LESS_THEN
    }
}
