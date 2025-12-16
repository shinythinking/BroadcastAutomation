package com.solapi.sdk.message.model.kakao

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 카카오 알림톡 템플릿의 메시지 유형을 정의합니다.
 */
@Serializable
enum class KakaoAlimtalkMessageType {
    /**
     * 기본 메시지 유형
     */
    @SerialName("BA")
    BASIC,

    /**
     * 부가 정보 메시지 유형
     */
    @SerialName("EX")
    EXTRA_INFO,

    /**
     * 채널 추가 메시지 유형
     */
    @SerialName("AD")
    CHANNEL_ADD,

    /**
     * 복합 메시지 유형
     */
    @SerialName("MI")
    MIXED
}
