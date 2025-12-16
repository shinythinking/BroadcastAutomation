package com.solapi.sdk.message.model.kakao

import kotlinx.serialization.Serializable

/**
 * 카카오 알림톡 템플릿의 버튼 정보를 정의하는 공통 모델입니다.
 * 요청 및 응답 DTO에서 모두 사용됩니다.
 */
@Serializable
data class KakaoAlimtalkTemplateButton(
    /**
     * 버튼 유형
     */
    var buttonType: KakaoButtonType? = null,

    /**
     * 버튼 이름
     */
    var buttonName: String? = null,

    /**
     * 모바일 링크
     */
    var linkMo: String? = null,

    /**
     * PC 링크
     */
    var linkPc: String? = null,

    /**
     * 안드로이드 앱 링크
     */
    var linkAnd: String? = null,

    /**
     * iOS 앱 링크
     */
    var linkIos: String? = null,

    /**
     * 추가 정보
     */
    var chatExtra: String? = null,

    /**
     * 외부 브라우저 사용 여부 (요청 시)
     * 응답에서는 Boolean? 타입으로 제공되므로, 응답 DTO에서 별도 처리가 필요합니다.
     */
    var targetOut: String? = null
)
