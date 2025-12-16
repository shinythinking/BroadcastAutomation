package com.solapi.sdk.message.model

/**
 * 메시지 조회를 위한 Custom Message Status.
 * 실제 메시지 발송 API 규격에 따르지 않아 주의가 필요합니다.
 * Message 모델의 status와는 다른 값입니다!
 */
enum class MessageStatusType {
    /**
     * 대기 상태, 상태코드 2000
     */
    PENDING,

    /**
     * 발송 중 상태, 상태코드 3000
     */
    SENDING,

    /**
     * 발송 완료 상태, 상태코드 4000
     */
    COMPLETE,

    /**
     * 발송 실패(접수 실패 포함) 상태, 상태코드가 2000, 3000, 4000번이 아닌 모든 상태코드
     */
    FAILED
}