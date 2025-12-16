package com.solapi.sdk.message.model.kakao

import kotlinx.serialization.Serializable

/**
 * 카카오 알림톡 템플릿의 강조 표시 정보를 정의하는 공통 모델입니다.
 * 요청 및 응답 DTO에서 모두 사용됩니다.
 */
@Serializable
data class KakaoAlimtalkTemplateHighlight(
    /**
     * 강조 표시 제목
     */
    var title: String? = null,

    /**
     * 강조 표시 설명
     */
    var description: String? = null,

    /**
     * 이미지 ID
     */
    var imageId: String? = null
)
