package com.solapi.sdk.message.dto.request.kakao

import com.solapi.sdk.message.dto.request.kakao.KakaoTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.GREATER_THEN
import com.solapi.sdk.message.dto.request.kakao.KakaoTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.GREATER_THEN_OR_EQUAL
import com.solapi.sdk.message.dto.request.kakao.KakaoTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.LESS_THEN
import com.solapi.sdk.message.dto.request.kakao.KakaoTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.LESS_THEN_OR_EQUAL
import com.solapi.sdk.message.dto.request.kakao.KakaoTemplateNameQuery.KakaoAlimtalkTemplateNameQueryCondition.EQUALS
import com.solapi.sdk.message.dto.request.kakao.KakaoTemplateNameQuery.KakaoAlimtalkTemplateNameQueryCondition.LIKE
import com.solapi.sdk.message.dto.request.kakao.KakaoTemplateNameQuery.KakaoAlimtalkTemplateNameQueryCondition.NOT_EQUALS
import com.solapi.sdk.message.model.kakao.KakaoAlimtalkTemplateStatus
import com.solapi.sdk.message.dto.request.kakao.KakaoTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.EQUALS as DATE_EQUALS

data class KakaoAlimtalkTemplateListRequest(
    var channelId: String? = null,
    var channelGroupId: String? = null,
    var name: KakaoTemplateNameQuery? = null,
    var templateId: String? = null,
    var isHidden: Boolean? = false,
    var status: KakaoAlimtalkTemplateStatus? = null,
    var isMine: Boolean? = false,
    var startKey: String? = null,
    var limit: Int? = null,
    var dateCreated: KakaoTemplateDateQuery? = null,
    var dateUpdated: KakaoTemplateDateQuery? = null,
) {
    fun generateQueryParams(): Map<String, String> {
        val params = mutableMapOf<String, String>()

        this.channelGroupId?.let { params["channelGroupId"] = it }
        this.channelId?.let { params["channelId"] = it }
        this.name?.let { it ->
            when (it.queryCondition) {
                EQUALS -> params["name[eq]"] = it.name
                NOT_EQUALS -> params["name[ne]"] = it.name
                LIKE -> params["name[like]"] = it.name
            }
        }
        this.templateId?.let { params["templateId"] = it }
        this.isHidden?.let { params["isHidden"] = it.toString() }
        this.status?.let { params["status"] = it.toString() }
        this.isMine?.let { params["isMine"] = it.toString() }
        this.startKey?.let { params["startKey"] = it }
        this.limit?.let { params["limit"] = it.toString() }
        this.dateCreated?.let { it ->
            when (it.queryCondition) {
                DATE_EQUALS -> params["dateCreated[eq]"] = it.date.toString()
                GREATER_THEN_OR_EQUAL -> params["dateCreated[gte]"] = it.date.toString()
                GREATER_THEN -> params["dateCreated[gt]"] = it.date.toString()
                LESS_THEN_OR_EQUAL -> params["dateCreated[lte]"] = it.date.toString()
                LESS_THEN -> params["dateCreated[lt]"] = it.date.toString()
            }
        }
        this.dateUpdated?.let { it ->
            when (it.queryCondition) {
                DATE_EQUALS -> params["dateUpdated[eq]"] = it.date.toString()
                GREATER_THEN_OR_EQUAL -> params["dateUpdated[gte]"] = it.date.toString()
                GREATER_THEN -> params["dateUpdated[gt]"] = it.date.toString()
                LESS_THEN_OR_EQUAL -> params["dateUpdated[lte]"] = it.date.toString()
                LESS_THEN -> params["dateUpdated[lt]"] = it.date.toString()
            }
        }

        return params
    }
}
