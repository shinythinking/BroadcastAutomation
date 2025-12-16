package com.solapi.sdk.message.model.kakao

import kotlinx.serialization.Serializable

/**
 * 카카오 알림톡 템플릿의 할당 유형을 정의합니다.
 */
@Serializable
enum class KakaoTemplateAssignType {
    /**
     * 채널 할당
     */
    CHANNEL,

    /**
     * 그룹 할당
     */
    GROUP
}
