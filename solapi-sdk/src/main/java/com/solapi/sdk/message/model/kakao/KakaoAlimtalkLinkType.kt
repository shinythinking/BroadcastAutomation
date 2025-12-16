package com.solapi.sdk.message.model.kakao

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 카카오 알림톡 템플릿의 바로가기 링크 유형을 정의합니다.
 */
@Serializable
enum class KakaoAlimtalkLinkType {
    /**
     * 웹 링크
     */
    @SerialName("WL")
    WEB_LINK,

    /**
     * 앱 링크
     */
    @SerialName("AL")
    APP_LINK,

    /**
     * 봇 키워드
     */
    @SerialName("BK")
    BOT_KEYWORD,

    /**
     * 메시지 전달
     */
    @SerialName("MD")
    MESSAGE_DELIVERY,

    /**
     * 비즈니스 채널
     */
    @SerialName("BC")
    BUSINESS_CHANNEL,

    /**
     * 봇 전환
     */
    @SerialName("BT")
    BOT_TRANSFER
}
