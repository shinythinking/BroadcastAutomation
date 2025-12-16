package com.solapi.sdk.message.dto.request.kakao

import kotlinx.serialization.Serializable

@Serializable
data class KakaoAlimtalkSendableTemplateListRequest(
    var channelId: String? = null,
    var templateId: String? = null
) {
    fun generateToQueryParams(): Map<String, String> {
        val map = mutableMapOf<String, String>()

        this.templateId?.let { map.put("templateId", it) }
        this.channelId?.let { map.put("channelId", it) }

        return map
    }
}
