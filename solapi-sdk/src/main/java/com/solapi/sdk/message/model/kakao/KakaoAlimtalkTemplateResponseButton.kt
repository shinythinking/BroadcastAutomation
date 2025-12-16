package com.solapi.sdk.message.model.kakao

import kotlinx.serialization.Serializable

/**
 * 카카오 알림톡 템플릿 응답의 버튼 정보를 정의합니다.
 * 'targetOut' 필드의 타입을 Boolean?으로 처리하여 응답에 특화된 모델입니다.
 */
@Serializable
data class KakaoAlimtalkTemplateResponseButton(
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
     * 외부 브라우저 사용 여부 (응답 시)
     */
    var targetOut: Boolean? = false
)
