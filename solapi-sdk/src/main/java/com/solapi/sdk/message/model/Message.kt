package com.solapi.sdk.message.model

import com.solapi.sdk.message.model.fax.FaxOption
import com.solapi.sdk.message.model.kakao.KakaoOption
import com.solapi.sdk.message.model.naver.NaverOption
import com.solapi.sdk.message.model.rcs.RcsOption
import com.solapi.sdk.message.model.voice.VoiceOption
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    /**
     * 카카오 알림톡, 친구톡 발송을 위한 파라미터
     */
    var kakaoOptions: KakaoOption? = null,

    /**
     * 네이버 스마트알림 발송을 위한 파라미터
     */
    var naverOptions: NaverOption? = null,

    /**
     * RCS 발송을 위한 파라미터
     */
    var rcsOptions: RcsOption? = null,

    /**
     * 메시지 발송 타입
     * 예) SMS, LMS, MMS, ATA...
     */
    var type: MessageType? = null,

    /**
     * 국가번호
     * +82, +1...
     */
    var country: String? = null,

    /**
     * LMS, MMS 발송 시 문자 내 제목
     */
    var subject: String? = null,

    /**
     * MMS 발송 시 시스템에 업로드 된 image ID
     */
    var imageId: String? = null,

    /**
     *     * 발송 접수일자
     */
    var dateProcessed: String? = null,

    /**
     * 통신사 결과 값 통보일자
     */
    var dateReported: String? = null,

    /**
     * 실제 메시지 발송 완료일자
     */
    var dateReceived: String? = null,

    /**
     * 메시지 상태코드
     */
    var statusCode: String? = null,

    /**
     * 대체 발송 여부
     */
    var replacement: Boolean? = null,

    /**
     * 메시지 타입 자동 구분 여부
     * 기본값: 허용(true)
     */
    var autoTypeDetect: Boolean? = null,

    /**
     * 문자 상태
     * 예) 대기, 접수, 발송완료 등
     * 해당 프로퍼티는 MessageStatusType과 다른 값입니다!
     */
    var status: String? = null,

    /**
     * 메시지 ID
     */
    var messageId: String? = null,

    /**
     * 그룹 ID
     */
    var groupId: String? = null,

    /**
     * 계정 고유번호
     */
    var accountId: String? = null,

    /**
     * 발송 할(된) 메시지 내용
     */
    var text: String? = null,

    /**
     * 메시지 생성일자
     */
    var dateCreated: String? = null,

    /**
     * 메시지 수정일자
     */
    var dateUpdated: String? = null,

    /**
     * 수신번호
     */
    var to: String? = null,

    /**
     * 발신번호, 반드시 계정 내 등록하신 발신번호를 입력하셔야 합니다.
     * 예) 029302266
     */
    var from: String? = null,

    /**
     * 사용자(누리고 서비스 이용자)를 위한 발송 요청 시 커스텀 값을 넣을 수 있는 필드
     * 메시지 조회 시에도 표시됩니다!
     */
    var customFields: MutableMap<String, String>? = null,

    /**
     * 대체발송 파라미터
     */
    var replacements: List<Message>? = null,

    /**
     * 팩스 발송용 옵션
     *
     * 팩스 발송은 먼저 SOLAPI 서버에 이미지 파일을 업로드해야 합니다.
     * @see com.solapi.sdk.message.service.DefaultMessageService.uploadFile
     */
    var faxOptions: FaxOption? = null,

    /**
     * 음성 메시지 발송용 옵션
     */
    var voiceOptions: VoiceOption? = null
) {

    init {
        from = from?.replace("-", "")
        to = to?.replace("-", "")
    }
}
