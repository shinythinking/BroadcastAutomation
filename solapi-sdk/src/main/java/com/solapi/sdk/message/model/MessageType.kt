package com.solapi.sdk.message.model

enum class MessageType {
    /**
     * 단문문자 (80 byte 미만)
     * */
    SMS,

    /**
     * 장문문자 (80 byte 이상, 2,000 byte 미만)
     */
    LMS,

    /**
     * 이미지가 포함된 문자 (80 byte 이상, 2,000 byte 미만), 200kb 이내 이미지 파일 1장 업로드 가능
     */
    MMS,

    /**
     * 카카오 알림톡
     * */
    ATA,

    /**
     * 카카오 친구톡
     */
    CTA,

    /**
     * 이미지가 포함된 카카오 친구톡(이미지 1장 업로드 가능)
     */
    CTI,

    /**
     * RCS 단문문자
     */
    RCS_SMS,

    /**
     * RCS 장문문자
     */
    RCS_LMS,

    /**
     * 이미지가 포함된 RCS 문자
     */
    RCS_MMS,

    /**
     * RCS 템플릿
     */
    RCS_TPL,

    /**
     * 네이버 스마트 알림(네이버 톡톡)
     */
    NSA,

    /**
     * 팩스
     */
    FAX,

    /**
     * 음성 메시지
     */
    VOICE,

    /**
     * 카카오 브랜드 메시지 텍스트 타입
     */
    BMS_TEXT,

    /**
     * 카카오 브랜드 메시지 이미지 타입
     */
    BMS_IMAGE,

    /**
     * 카카오 브랜드 메시지 와이드 타입
     */
    BMS_WIDE,

    /**
     * 카카오 브랜드 메시지 와이드 리스트 타입
     */
    BMS_WIDE_ITEM_LIST;
}