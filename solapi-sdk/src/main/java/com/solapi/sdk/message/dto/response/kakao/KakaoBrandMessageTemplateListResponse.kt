package com.solapi.sdk.message.dto.response.kakao

import com.solapi.sdk.message.dto.response.common.CommonListResponse
import com.solapi.sdk.message.model.kakao.KakaoBrandMessageTemplate
import kotlinx.serialization.Serializable

@Serializable
data class KakaoBrandMessageTemplateListResponse(
    var brandTemplateList: List<KakaoBrandMessageTemplate>? = null,
) : CommonListResponse()
