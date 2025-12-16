package com.solapi.sdk.message.model.kakao

import kotlinx.serialization.Serializable

@Serializable
data class KakaoAlimtalkTemplateCategory(
    /**
     * 카테고리 코드
     */
    var code: String? = null,

    /**
     * 카테고리 이름
     */
    var name: String? = null,

    /**
     * 카테고리 그룹 이름
     */
    var groupName: String? = null,

    /**
     * 카테고리 설명
     */
    var inclusion: String? = null,

    /**
     * 카테고리 유의사항
     */
    var exclusion: String? = null,
)
