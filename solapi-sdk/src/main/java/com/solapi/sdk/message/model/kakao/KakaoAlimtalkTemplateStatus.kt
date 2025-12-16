package com.solapi.sdk.message.model.kakao

import kotlinx.serialization.Serializable

/**
 * 카카오 알림톡 템플릿의 상태를 정의합니다.
 */
@Serializable
enum class KakaoAlimtalkTemplateStatus {
    /**
     * 검수 대기 상태
     */
    PENDING,

    /**
     * 검수중 상태
     */
    INSPECTING,

    /**
     * 검수 완료(승인) 상태
     */
    APPROVED,

    /**
     * 검수 반려 상태
     */
    REJECTED
}
