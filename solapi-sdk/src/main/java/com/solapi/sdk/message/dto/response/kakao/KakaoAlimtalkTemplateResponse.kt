package com.solapi.sdk.message.dto.response.kakao

import com.solapi.sdk.message.model.kakao.KakaoAlimtalkEmphasizeType
import com.solapi.sdk.message.model.kakao.KakaoAlimtalkMessageType
import com.solapi.sdk.message.model.kakao.KakaoAlimtalkTemplateCodeList
import com.solapi.sdk.message.model.kakao.KakaoAlimtalkTemplateHighlight
import com.solapi.sdk.message.model.kakao.KakaoAlimtalkTemplateItem
import com.solapi.sdk.message.model.kakao.KakaoAlimtalkTemplateResponseButton
import com.solapi.sdk.message.model.kakao.KakaoAlimtalkTemplateResponseQuickReply
import com.solapi.sdk.message.model.kakao.KakaoAlimtalkTemplateStatus
import com.solapi.sdk.message.model.kakao.KakaoTemplateAssignType
import kotlinx.serialization.Serializable

@Serializable
data class KakaoAlimtalkTemplateResponse(
    var templateId: String? = null,
    var name: String? = null,
    var channelId: String? = null,
    var channelGroupId: String? = null,
    var assignType: KakaoTemplateAssignType? = null,
    var accountId: String? = null,
    var securityFlag: Boolean? = false,
    var categoryCode: String? = null,
    var isHidden: Boolean? = null,
    var isDeleted: Boolean? = null,
    var content: String? = null,
    var buttons: List<KakaoAlimtalkTemplateResponseButton>? = null,
    var quickReplies: List<KakaoAlimtalkTemplateResponseQuickReply>? = null,
    var highlight: KakaoAlimtalkTemplateHighlight? = null,
    var item: KakaoAlimtalkTemplateItem? = null,
    var comments: List<KakaoAlimtalkTemplateComment>? = null,
    var code: String? = null,
    var codes: List<KakaoAlimtalkTemplateCodeList>? = null,
    var commentable: Boolean? = null,
    var status: KakaoAlimtalkTemplateStatus? = null,
    var messageType: KakaoAlimtalkMessageType? = null,
    var emphasizeType: KakaoAlimtalkEmphasizeType? = null,
    var extra: String? = null,
    var ad: String? = null,
    var emphasizeTitle: String? = null,
    var emphasizeSubtitle: String? = null,
    var imageId: String? = null,
    var header: String? = null,
    var variables: List<KakaoAlimtalkTemplateVariable>? = null,

    var dateCreated: String? = null,

    var dateUpdated: String? = null
) {
    @Serializable
    data class KakaoAlimtalkTemplateComment(
        var memberId: String,
        var content: String? = null,
        var isAdmin: Boolean? = false,
        var dateCreated: String? = null
    )

    @Serializable
    data class KakaoAlimtalkTemplateVariable(
        var name: String? = null
    )
}
