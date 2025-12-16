package com.solapi.sdk.message.service

import com.solapi.sdk.message.dto.request.FileUploadRequest
import com.solapi.sdk.message.dto.request.MultipleDetailMessageSendingRequest
import com.solapi.sdk.message.dto.request.kakao.KakaoAlimtalkTemplateMutationRequest
import com.solapi.sdk.message.dto.request.kakao.KakaoAlimtalkTemplateUpdateNameRequest
import com.solapi.sdk.message.dto.response.FileUploadResponse
import com.solapi.sdk.message.dto.response.MessageListResponse
import com.solapi.sdk.message.dto.response.MultipleDetailMessageSentResponse
import com.solapi.sdk.message.dto.response.kakao.KakaoAlimtalkTemplateListResponse
import com.solapi.sdk.message.dto.response.kakao.KakaoAlimtalkTemplateResponse
import com.solapi.sdk.message.dto.response.kakao.KakaoBrandMessageTemplateListResponse
import com.solapi.sdk.message.model.Balance
import com.solapi.sdk.message.model.Quota
import com.solapi.sdk.message.model.kakao.KakaoAlimtalkTemplateCategory
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface MessageHttpService : MessageService {

    @JvmSuppressWildcards
    @GET("/messages/v4/list")
    fun getMessageList(@QueryMap parameter: Map<String, Any?>? = null): Call<MessageListResponse>

    @POST("/messages/v4/send-many/detail")
    fun sendManyDetail(@Body parameter: MultipleDetailMessageSendingRequest): Call<MultipleDetailMessageSentResponse>

    @POST("/storage/v1/files")
    fun uploadFile(@Body fileUploadRequest: FileUploadRequest): Call<FileUploadResponse>

    @GET("/cash/v1/balance")
    fun getBalance(): Call<Balance>

    @GET("/quota/v1/me")
    fun getQuota(): Call<Quota>

    @GET("/kakao/v2/templates/categories")
    fun getKakaoAlimtalkTemplateCategories(): Call<List<KakaoAlimtalkTemplateCategory>>

    @POST("/kakao/v2/templates")
    fun createKakaoAlimtalkTemplate(@Body parameter: KakaoAlimtalkTemplateMutationRequest): Call<KakaoAlimtalkTemplateResponse>

    @PUT("/kakao/v2/templates/{templateId}")
    fun updateKakaoAlimtalkTemplate(
        @Path("templateId") templateId: String,
        @Body parameter: KakaoAlimtalkTemplateMutationRequest
    ): Call<KakaoAlimtalkTemplateResponse>

    @PUT("/kakao/v2/templates/{templateId}/inspection")
    fun requestKakaoAlimtalkTemplateInspection(@Path("templateId") templateId: String): Call<KakaoAlimtalkTemplateResponse>

    @GET("/kakao/v2/templates/{templateId}")
    fun getKakaoAlimtalkTemplate(@Path("templateId") templateId: String): Call<KakaoAlimtalkTemplateResponse>

    @GET("/kakao/v2/templates")
    fun getKakaoAlimtalkTemplates(@QueryMap(encoded = true) parameter: Map<String, String> = emptyMap()): Call<KakaoAlimtalkTemplateListResponse>

    @GET("/kakao/v2/templates/sendable")
    fun getSendableKakaoAlimtalkTemplates(@QueryMap parameter: Map<String, String> = emptyMap()): Call<List<KakaoAlimtalkTemplateResponse>>

    @PUT("/kakao/v2/templates/{templateId}/inspection/cancel")
    fun cancelKakaoAlimtalkTemplateInspection(@Path("templateId") templateId: String): Call<KakaoAlimtalkTemplateResponse>

    @DELETE("/kakao/v2/templates/{templateId}")
    fun removeKakaoAlimtalkTemplate(@Path("templateId") templateId: String): Call<KakaoAlimtalkTemplateResponse>

    @POST("/kakao/v2/templates/{templateId}/relese-dormant")
    fun requestKakaoAlimtalkTemplateReactivation(@Path("templateId") templateId: String): Call<KakaoAlimtalkTemplateResponse>

    @PUT("/kakao/v2/templates/{templateId}/name")
    fun updateKakaoAlimtalkTemplateName(
        @Path("templateId") templateId: String,
        @Body name: KakaoAlimtalkTemplateUpdateNameRequest
    ): Call<KakaoAlimtalkTemplateResponse>

    @GET("/kakao/v2/brand-templates")
    fun getKakaoBrandMessageTemplates(@QueryMap parameter: Map<String, String> = emptyMap()): Call<KakaoBrandMessageTemplateListResponse>
}