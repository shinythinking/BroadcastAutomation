package com.solapi.sdk.message.model.kakao

import kotlinx.serialization.Serializable

@Serializable
enum class KakaoButtonType {
    /**
     * 웹링크
     * 해당 버튼 타입을 사용할 경우 buttons 내 linkMo, linkPc 값을 반드시 기입하셔야 합니다.
     */
    WL,

    /**
     * 앱링크
     * 해당 버튼 타입을 사용할 경우 buttons 내 linkIos, linkAnd 값을 반드시 기입하셔야 합니다.
     */
    AL,

    /**
     * 봇키워드
     * 해당 버튼 텍스트 전송
     */
    BK,

    /**
     * 메시지전달
     * 해당 버튼 텍스트 + 메시지 본문 전송
     */
    MD,

    /**
     * 배송조회
     * 버튼 클릭 시 배송 조회 페이지로 이동
     * 알림톡만 이용 가능, 대한통운 지원안함
     */
    DS,

    /**
     * 상담톡 전환
     * 상담톡 서비스를 이용하고 있을 경우 상담톡으로 전환
     * @see <a href="https://business.kakao.com/info/bizmessage/">상담톡 딜러사 확인</a>
     */
    BC,

    /**
     * 봇전환
     * 채널 봇으로 전환
     */
    BT,

    /**
     * 채널 추가
     * 카카오톡 채널을 추가하는 버튼
     */
    AC
}