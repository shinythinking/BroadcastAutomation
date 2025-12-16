package com.solapi.sdk.message.model

import kotlinx.serialization.Serializable

@Serializable
data class Balance(
    /**
     * 충전금액
     */
    var balance: Float? = null,

    /**
     * 포인트(충전금액과 가치 동일)
     */
    var point: Float? = null,
)
