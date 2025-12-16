package com.solapi.sdk.message.dto.request.kakao

import com.solapi.sdk.message.dto.request.kakao.KakaoTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.GREATER_THEN
import com.solapi.sdk.message.dto.request.kakao.KakaoTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.GREATER_THEN_OR_EQUAL
import com.solapi.sdk.message.dto.request.kakao.KakaoTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.LESS_THEN
import com.solapi.sdk.message.dto.request.kakao.KakaoTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.LESS_THEN_OR_EQUAL
import com.solapi.sdk.message.dto.request.kakao.KakaoTemplateNameQuery.KakaoAlimtalkTemplateNameQueryCondition.EQUALS
import com.solapi.sdk.message.dto.request.kakao.KakaoTemplateNameQuery.KakaoAlimtalkTemplateNameQueryCondition.LIKE
import com.solapi.sdk.message.dto.request.kakao.KakaoTemplateNameQuery.KakaoAlimtalkTemplateNameQueryCondition.NOT_EQUALS
import com.solapi.sdk.message.model.kakao.KakaoBrandMessageTemplate
import com.solapi.sdk.message.dto.request.kakao.KakaoTemplateDateQuery.KakaoAlimtalkTemplateDateQueryCondition.EQUALS as DATE_EQUALS

data class KakaoBrandMessageTemplateListRequest(
    var name: KakaoTemplateNameQuery? = null,
    var pfId: String? = null,
    var pfGroupId: String? = null,
    var brandTemplateId: String? = null,
    var chatBubbleType: KakaoBrandMessageTemplate.ChatBubbleType? = null,
    var startKey: String? = null,
    var limit: Int? = null,
    var dateCreated: KakaoTemplateDateQuery? = null,
    var dateUpdated: KakaoTemplateDateQuery? = null,
) {
    fun generateQueryParams(): Map<String, String> {
        val params = mutableMapOf<String, String>()

        this.pfGroupId?.let { params["pfGroupId"] = it }
        this.pfId?.let { params["pfId"] = it }
        this.name?.let { it ->
            when (it.queryCondition) {
                EQUALS -> params["name[eq]"] = it.name
                NOT_EQUALS -> params["name[ne]"] = it.name
                LIKE -> params["name[like]"] = it.name
            }
        }
        this.brandTemplateId?.let { params["brandTemplateId"] = it }
        this.chatBubbleType?.let { params["chatBubbleType"] = it.toString() }
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