package com.solapi.sdk.message.dto.request

import com.solapi.sdk.message.model.CommonMessageProperty
import com.solapi.sdk.message.model.MessageStatusType
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class MessageListRequest(
    /**
     * 수신번호
     */
    override var to: String? = null,

    /**
     * 발신번호
     */
    override var from: String? = null,

    /**
     * Pagination을 위한 key
     * 조회 후 다음 페이지로 넘어가려면 조회 당시 마지막의 messageId를 입력해주셔야 합니다.
     */
    var startKey: String? = null,

    /**
     * 조회할 건 수
     */
    var limit: Int? = null,

    /**
     * 메시지 ID
     */
    var messageId: String? = null,

    /**
     * 메시지 ID 목록
     */
    var messageIds: List<String>? = null,

    /**
     * 메시지 그룹 ID
     */
    var groupId: String? = null,

    /**
     * 메시지 유형 (예) ATA, SMS 등)
     */
    var type: String? = null,

    /**
     * 상태코드 (예) -> 2000: 발송 대기, 3000: 발송 중, 4000: 발송완료)
     */
    var statusCode: String? = null,

    /**
     * 조회 할 시작 날짜
     */
    @Contextual
    var startDate: LocalDateTime? = null,

    /**
     * 조회 할 종료 날짜
     */
    @Contextual
    var endDate: LocalDateTime? = null,

    /**
     * 발송 상태
     */
    var status: MessageStatusType? = null
) : CommonMessageProperty
