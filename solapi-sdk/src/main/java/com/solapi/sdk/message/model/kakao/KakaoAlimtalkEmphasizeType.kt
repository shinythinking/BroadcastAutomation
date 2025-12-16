package com.solapi.sdk.message.model.kakao

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 카카오 알림톡 템플릿의 강조 유형을 정의합니다.
 */
@Serializable
enum class KakaoAlimtalkEmphasizeType {
    /**
     * 강조 없음
     */
    @SerialName("NONE")
    NONE,

    /**
     * 텍스트 강조
     */
    @SerialName("TEXT")
    TEXT,

    /**
     * 이미지 강조
     */
    @SerialName("IMAGE")
    IMAGE,

    /**
     * 아이템 리스트 강조
     */
    @SerialName("ITEM_LIST")
    ITEM_LIST
}
