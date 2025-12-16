package com.solapi.sdk.message.model.kakao

import kotlinx.serialization.Serializable

/**
 * 카카오 알림톡 템플릿의 아이템 정보를 정의하는 공통 모델입니다.
 */
@Serializable
data class KakaoAlimtalkTemplateItem(
    /**
     * 아이템 목록
     */
    var list: List<KakaoAlimtalkTemplateItemList>? = null,

    /**
     * 아이템 요약 정보
     */
    var summary: KakaoAlimtalkTemplateItemSummary? = null
) {
    /**
     * 카카오 알림톡 템플릿의 아이템 리스트 정보를 정의합니다.
     */
    @Serializable
    data class KakaoAlimtalkTemplateItemList(
        /**
         * 아이템 리스트 제목
         */
        var title: String? = null,

        /**
         * 아이템 리스트 설명
         */
        var description: String? = null
    )

    /**
     * 카카오 알림톡 템플릿의 아이템 요약 정보를 정의합니다.
     */
    @Serializable
    data class KakaoAlimtalkTemplateItemSummary(
        /**
         * 아이템 요약 제목
         */
        var title: String? = null,

        /**
         * 아이템 요약 설명
         */
        var description: String? = null
    )
}
