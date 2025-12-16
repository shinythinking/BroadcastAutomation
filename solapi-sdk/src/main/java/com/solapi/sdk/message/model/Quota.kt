package com.solapi.sdk.message.model

import kotlinx.serialization.Serializable

@Serializable
data class Quota(
    /**
     * 계정 아이디
     */
    var accountId: String? = null,

    /**
     * 발송량 자동 조정 여부
     */
    var autoAdjustment: Boolean? = null,

    /**
     * 일일 발송량
     */
    var quota: Int? = null,

    /**
     * 최소 일일 발송량
     */
    var min: Int? = null,

    /**
     * 최대 일일 발송량
     */
    var max: Int? = null
)