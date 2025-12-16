package com.solapi.sdk.message.dto.request.kakao

import com.solapi.sdk.message.model.kakao.KakaoAlimtalkEmphasizeType
import com.solapi.sdk.message.model.kakao.KakaoAlimtalkMessageType
import com.solapi.sdk.message.model.kakao.KakaoAlimtalkTemplateButton
import com.solapi.sdk.message.model.kakao.KakaoAlimtalkTemplateHighlight
import com.solapi.sdk.message.model.kakao.KakaoAlimtalkTemplateItem
import com.solapi.sdk.message.model.kakao.KakaoAlimtalkTemplateQuickReply
import kotlinx.serialization.Serializable

/**
 * 카카오 알림톡 템플릿 생성/수정 요청 데이터 클래스
 */
@Serializable
data class KakaoAlimtalkTemplateMutationRequest(
    /**
     * 카카오톡 채널 고유 아이디. 채널에 템플릿을 등록하고 싶을 경우 필수입니다.
     */
    var channelId: String? = null,

    /**
     * 카카오톡 채널 템플릿 그룹 고유 아이디. 그룹에 템플릿을 등록하고 싶을 경우 필수입니다.
     */
    var channelGroupId: String? = null,

    /**
     * 템플릿 이름. 다른 알림톡 템플릿과 중복 가능합니다.
     */
    var name: String? = null,

    /**
     * 템플릿 내용. 변수 포함 가능합니다. 변수 치환 후 1000자를 넘을 수 없습니다.
     */
    var content: String? = null,

    /**
     * 카카오 템플릿 카테고리 코드.
     * https://developers.solapi.com/references/kakao/templates/getCategories
     */
    var categoryCode: String? = null,

    /**
     * 템플릿에 들어가는 버튼들
     */
    var buttons: List<KakaoAlimtalkTemplateButton>? = null,

    /**
     * 바로 연결. 최소 1개, 최대 10개.
     */
    var quickReplies: List<KakaoAlimtalkTemplateQuickReply>? = null,

    /**
     * 메시지 유형(BA: 기본형, EX: 부가정보형, AD: 채널추가형, MI: 복합형).
     */
    var messageType: KakaoAlimtalkMessageType? = null,

    /**
     * 강조 유형(NONE: 선택안함, TEXT: 강조표기형, IMAGE: 이미지형, ITEM_LIST: 아이템리스트형).
     */
    var emphasizeType: KakaoAlimtalkEmphasizeType? = null,

    /**
     * 알림톡 헤더. emphasizeType이 ITEM_LIST일 때만 사용 가능합니다. 변수 포함 가능. 최대 16자
     */
    var header: String? = null,

    /**
     * 알림톡 하이라이트. emphasizeType이 ITEM_LIST일 때만 사용 가능합니다.
     */
    var highlight: KakaoAlimtalkTemplateHighlight? = null,

    /**
     * 알림톡 아이템. 목록과 요약이 있습니다. 강조 유형이 아이템 리스트일 때만 사용 가능합니다.
     */
    var item: KakaoAlimtalkTemplateItem? = null,

    /**
     * 부가정보. 변수 포함 불가능. 최대 500자
     */
    var extra: String? = null,

    /**
     * 	채널 추가 문구. 문구는 채널 추가하고 이 채널의 광고와 마케팅 메시지를 카카오톡으로 받기 고정입니다.
     */
    var ad: String? = null,

    /**
     * 강조 표기 문구. 변수가 들어갈 수 있습니다. emphasizeType이 TEXT일 때만 사용할 수 있습니다. 최대 50자 입력가능
     */
    var emphasizeTitle: String? = null,

    /**
     * 강조 표기 보조문구. 변수가 들어갈 수 없습니다. emphasizeType이 TEXT일 경우 필수. 최대 50자 입력가능
     */
    var emphasizeSubtitle: String? = null,

    /**
     * 보안 템플릿 여부. true일 경우 해당 템플릿을 PC에서는 확인할 수 없습니다. 기본값: false
     */
    var securityFlag: Boolean = false,

    /**
     * 	알림톡에 사용되는 이미지 고유 아이디
     */
    var imageId: String? = null
)
