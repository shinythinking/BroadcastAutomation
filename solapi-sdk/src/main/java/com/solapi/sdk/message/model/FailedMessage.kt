package com.solapi.sdk.message.model

import kotlinx.serialization.Serializable

@Serializable
data class FailedMessage(
    /**
     * 수신번호
     */
    var to: String? = null,
    /**
     * 발신번호
     */
    var from: String? = null,
    /**
     * 메시지 유형
     * 예) SMS, LMS, ATA(알림톡)... etc
     */
    var type: String? = null,
    /**
     * 국가번호
     * 예) "1" -> 미국 국가번호
     */
    var country: String? = null,
    /**
     * 메시지 ID
     * 메시지 ID는 메시지 조회 등에 사용할 수 있습니다.
     */
    var messageId: String? = null,
    /**
     * 상태 코드
     * 예) 1062 -> 발신번호 미등록
     */
    var statusCode: String? = null,
    /**
     * 상태 메시지
     * 예) 발신번호 미등록
     */
    var statusMessage: String? = null,
    /**
     * 계정 고유번호
     */
    var accountId: String? = null,

    /**
     * 사용자가 지정한 커스텀 필드 값
     */
    var customFields: Map<String, String>? = null
)
