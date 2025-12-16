package com.solapi.sdk.message.dto.response.kakao

import com.solapi.sdk.message.dto.response.common.CommonListResponse
import kotlinx.serialization.Serializable

@Serializable
data class KakaoAlimtalkTemplateListResponse(
    var templateList: List<KakaoAlimtalkTemplateResponse>? = null,
) : CommonListResponse()
