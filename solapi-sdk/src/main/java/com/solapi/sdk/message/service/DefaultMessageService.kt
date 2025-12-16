package com.solapi.sdk.message.service

import com.solapi.sdk.message.dto.request.FileUploadRequest
import com.solapi.sdk.message.dto.request.MessageListBaseRequest
import com.solapi.sdk.message.dto.request.MessageListRequest
import com.solapi.sdk.message.dto.request.MultipleDetailMessageSendingRequest
import com.solapi.sdk.message.dto.request.SendRequestConfig
import com.solapi.sdk.message.dto.request.kakao.KakaoAlimtalkSendableTemplateListRequest
import com.solapi.sdk.message.dto.request.kakao.KakaoAlimtalkTemplateListRequest
import com.solapi.sdk.message.dto.request.kakao.KakaoAlimtalkTemplateMutationRequest
import com.solapi.sdk.message.dto.request.kakao.KakaoAlimtalkTemplateUpdateNameRequest
import com.solapi.sdk.message.dto.request.kakao.KakaoBrandMessageTemplateListRequest
import com.solapi.sdk.message.dto.response.ErrorResponse
import com.solapi.sdk.message.dto.response.MessageListResponse
import com.solapi.sdk.message.dto.response.MultipleDetailMessageSentResponse
import com.solapi.sdk.message.dto.response.kakao.KakaoAlimtalkTemplateListResponse
import com.solapi.sdk.message.dto.response.kakao.KakaoAlimtalkTemplateResponse
import com.solapi.sdk.message.dto.response.kakao.KakaoBrandMessageTemplateListResponse
import com.solapi.sdk.message.exception.SolapiBadRequestException
import com.solapi.sdk.message.exception.SolapiEmptyResponseException
import com.solapi.sdk.message.exception.SolapiFileUploadException
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException
import com.solapi.sdk.message.exception.SolapiUnknownException
import com.solapi.sdk.message.lib.Authenticator
import com.solapi.sdk.message.lib.JsonSupport
import com.solapi.sdk.message.lib.MapHelper
import com.solapi.sdk.message.lib.addMessageListParameterToCriteria
import com.solapi.sdk.message.lib.handleApiResponse
import com.solapi.sdk.message.lib.handleErrorResponse
import com.solapi.sdk.message.lib.processSendRequest
import com.solapi.sdk.message.model.Balance
import com.solapi.sdk.message.model.Message
import com.solapi.sdk.message.model.MessageStatusType
import com.solapi.sdk.message.model.Quota
import com.solapi.sdk.message.model.StorageType
import com.solapi.sdk.message.model.kakao.KakaoAlimtalkTemplateCategory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.commons.codec.binary.Base64
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.File
import java.io.FileInputStream

class DefaultMessageService(apiKey: String, apiSecretKey: String, domain: String) : MessageService {
    private var messageHttpService: MessageHttpService

    init {
        val client = OkHttpClient.Builder()
            .connectTimeout(50, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(50, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(50, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val authInfo = Authenticator(apiKey, apiSecretKey).generateAuthInfo()
                val request: Request =
                    chain.request().newBuilder().addHeader("Authorization", authInfo).build()
                chain.proceed(request)
            }.build()
        val contentType = "application/json".toMediaType()

        messageHttpService =
            Retrofit.Builder().baseUrl(domain)
                .addConverterFactory(JsonSupport.json.asConverterFactory(contentType))
                .client(client).build().create(MessageHttpService::class.java)
    }


    /**
     * 파일 업로드 메소드
     * 파일을 접근 가능한 경로로 입력하셔야 합니다.
     * */
    @Throws
    @JvmOverloads
    fun uploadFile(
        file: File,
        fileType: StorageType = StorageType.MMS,
        link: String? = null
    ): String? {
        val length = file.length()
        val imageByte = ByteArray(length.toInt())
        val fis: FileInputStream?
        try {
            fis = FileInputStream(file)
            fis.read(imageByte)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val encodedFile = String(Base64.encodeBase64(imageByte))
        val fileRequest = FileUploadRequest(
            file = encodedFile, type = fileType, link = link
        )
        val response = this.messageHttpService.uploadFile(fileRequest).execute()
        if (response.isSuccessful) {
            return response.body()?.fileId
        } else {
            // 파일 업로드는 특별한 예외를 던지므로 공통 에러 핸들러를 사용하지 않음
            val errorResponse: ErrorResponse =
                JsonSupport.json.decodeFromString(response.errorBody()?.string() ?: "")
            throw SolapiFileUploadException(errorResponse.errorMessage)
        }
    }

    /**
     * 메시지 조회 메소드
     */
    @JvmOverloads
    fun getMessageList(parameter: MessageListRequest? = null): MessageListResponse? {
        val payload = parameter?.let { it ->
            val tempPayload = MessageListBaseRequest()

            if (it.status != null && !it.statusCode.isNullOrBlank()) {
                throw SolapiBadRequestException("status와 statusCode는 병기할 수 없습니다.")
            }

            it.status?.let { status ->
                val (criteria, cond, value) = when (status) {
                    MessageStatusType.PENDING -> Triple("statusCode", "eq", "2000")
                    MessageStatusType.SENDING -> Triple("statusCode", "eq", "3000")
                    MessageStatusType.COMPLETE -> Triple("statusCode", "eq", "4000")
                    MessageStatusType.FAILED -> Triple(
                        "statusCode,statusCode,statusCode",
                        "ne,ne,ne",
                        "2000,3000,4000"
                    )
                }
                tempPayload.criteria = criteria
                tempPayload.cond = cond
                tempPayload.value = value
            }

            it.messageIds?.takeIf { it.isNotEmpty() }?.let { ids ->
                val messageIdCriteria = "messageId"
                val eqCond = "eq"
                tempPayload.criteria =
                    (tempPayload.criteria?.let { "$it," }
                        ?: "") + ids.joinToString(",") { messageIdCriteria }
                tempPayload.cond =
                    (tempPayload.cond?.let { "$it," } ?: "") + ids.joinToString(",") { eqCond }
                tempPayload.value =
                    (tempPayload.value?.let { "$it," } ?: "") + ids.joinToString(",")
            }

            listOf(
                "to" to it.to,
                "from" to it.from,
                "messageId" to it.messageId,
                "groupId" to it.groupId,
                "type" to it.type,
                "statusCode" to it.statusCode
            ).forEach { (field, value) ->
                addMessageListParameterToCriteria(tempPayload, field, value)
            }

            tempPayload.startKey = it.startKey
            tempPayload.limit = it.limit
            tempPayload.startDate = it.startDate
            tempPayload.endDate = it.endDate

            MapHelper.toMap(tempPayload)
        } ?: emptyMap<String, Any>()

        val response = this.messageHttpService.getMessageList(payload).execute()

        return if (response.isSuccessful) {
            response.body()
        } else {
            handleErrorResponse(response.errorBody()?.string())
        }
    }

    /**
     * 단일, 다중 메시지 발송 메소드
     * sendOne 및 sendMany 보다 더 개선된 오류 및 데이터 정보를 반환합니다.
     * SendRequestConfig 파라미터를 통해 예약발송, 중복 수신번호 허용, 메시지 리스트 표시 옵션을 활성화/비활성화 할 수 있습니다.
     */
    @JvmOverloads
    @Throws(
        SolapiMessageNotReceivedException::class,
        SolapiEmptyResponseException::class,
        SolapiUnknownException::class
    )
    fun send(
        message: Message,
        sendRequestConfig: SendRequestConfig? = null,
    ): MultipleDetailMessageSentResponse {
        return send(listOf(message), sendRequestConfig)
    }

    /**
     * 단일, 다중 메시지 발송 메소드
     * sendOne 및 sendMany 보다 더 개선된 오류 및 데이터 정보를 반환합니다.
     * SendRequestConfig 파라미터를 통해 예약발송, 중복 수신번호 허용, 메시지 리스트 표시 옵션을 활성화/비활성화 할 수 있습니다.
     */
    @JvmOverloads
    @Throws(
        SolapiMessageNotReceivedException::class,
        SolapiEmptyResponseException::class,
        SolapiUnknownException::class
    )
    fun send(
        messages: List<Message>,
        sendRequestConfig: SendRequestConfig? = null,
    ): MultipleDetailMessageSentResponse {
        if (messages.isEmpty()) {
            throw SolapiBadRequestException("메시지가 1건 이상 등록되어야 합니다.")
        }
        if (messages.size > 10000) {
            throw SolapiBadRequestException("10,000건 이상의 메시지는 한 번에 발송할 수 없습니다.")
        }

        val parameter = MultipleDetailMessageSendingRequest(
            messages = messages,
            scheduledDate = sendRequestConfig?.scheduledDate,
            showMessageList = sendRequestConfig?.showMessageList == true
        )
        if (sendRequestConfig?.allowDuplicates == true) {
            parameter.allowDuplicates = true
        }
        return processSendRequest(this.messageHttpService, parameter)
    }

    /**
     * 잔액 조회 메소드
     */
    @Throws
    fun getBalance(): Balance {
        val response = this.messageHttpService.getBalance().execute()
        return handleApiResponse(response, "잔액 조회에 실패했습니다.")
    }

    /**
     * 일일 발송량 한도 조회 메소드
     */
    @Throws
    fun getQuota(): Quota {
        val response = this.messageHttpService.getQuota().execute()
        return handleApiResponse(response, "일일 발송량 조회에 실패했습니다.")
    }

    /**
     * 카카오 알림톡 템플릿 카테고리 조회 메소드
     *
     * 카카오 알림톡 템플릿을 생성하기 전에 해당 카테고리를 조회하신 다음, 조회한 값을 카카오 알림톡 템플릿 생성 메소드에 파라미터로 같이 넣어주셔야 합니다.
     */
    @Throws
    fun getKakaoAlimtalkTemplateCategories(): List<KakaoAlimtalkTemplateCategory> {
        val response = this.messageHttpService.getKakaoAlimtalkTemplateCategories().execute()
        return handleApiResponse(response, "카카오 알림톡 템플릿 카테고리 조회에 실패했습니다.")
    }

    /**
     * 카카오 알림톡 템플릿 생성 메소드
     */
    @Throws
    fun createKakaoAlimtalkTemplate(parameter: KakaoAlimtalkTemplateMutationRequest): KakaoAlimtalkTemplateResponse {
        val response = this.messageHttpService.createKakaoAlimtalkTemplate(parameter).execute()
        return handleApiResponse(
            response,
            "카카오 알림톡 템플릿 생성에 실패했습니다, 에러가 반복되는 경우 SOLAPI 측 관리자에게 문의해주세요."
        )
    }

    /**
     * 카카오 알림톡 템플릿 목록 조회 메소드
     */
    @Throws
    @JvmOverloads
    fun getKakaoAlimtalkTemplates(parameter: KakaoAlimtalkTemplateListRequest? = null): KakaoAlimtalkTemplateListResponse {
        val queryParams = parameter?.generateQueryParams() ?: emptyMap()
        val response = this.messageHttpService.getKakaoAlimtalkTemplates(queryParams).execute()
        return handleApiResponse(response, "카카오 알림톡 템플릿 목록 조회에 실패했습니다.")
    }

    /**
     * 카카오 알림톡 템플릿 조회 메소드
     *
     * 카카오 알림톡 템플릿 조회 메소드를 호출 할 경우 해당 템플릿의 휴면 상태 또한 실시간으로 갱신됩니다. 만일 조회한 템플릿이 휴면 상태일 경우 휴면해지 요청 메소드를 통해 휴면 상태를 해지하실 수 있습니다.
     * @see requestKakaoAlimtalkTemplateReactivation
     */
    @Throws
    fun getKakaoAlimtalkTemplate(templateId: String): KakaoAlimtalkTemplateResponse {
        val response = this.messageHttpService.getKakaoAlimtalkTemplate(templateId).execute()
        return handleApiResponse(response, "카카오 알림톡 템플릿 조회에 실패했습니다.")
    }

    /**
     * 카카오 알림톡 템플릿 휴면 해지요청 메소드
     */
    @Throws
    fun requestKakaoAlimtalkTemplateReactivation(templateId: String): KakaoAlimtalkTemplateResponse {
        val response =
            this.messageHttpService.requestKakaoAlimtalkTemplateReactivation(templateId).execute()
        return handleApiResponse(response, "카카오 알림톡 템플릿 휴면 해지요청에 실패했습니다.")
    }

    /**
     * 발송 가능한 카카오 알림톡 템플릿 목록 조회 메소드
     */
    @Throws
    @JvmOverloads
    fun getSendableKakaoAlimtalkTemplates(parameter: KakaoAlimtalkSendableTemplateListRequest? = null): List<KakaoAlimtalkTemplateResponse> {
        val queryParams = parameter?.generateToQueryParams() ?: emptyMap()
        val response =
            this.messageHttpService.getSendableKakaoAlimtalkTemplates(queryParams).execute()
        return handleApiResponse(response, "발송 가능한 카카오 알림톡 템플릿 목록 조회에 실패했습니다.")
    }

    @Throws
    fun updateKakaoAlimtalkTemplate(
        templateId: String,
        parameter: KakaoAlimtalkTemplateMutationRequest
    ): KakaoAlimtalkTemplateResponse {
        val response =
            this.messageHttpService.updateKakaoAlimtalkTemplate(templateId, parameter).execute()
        return handleApiResponse(response, "카카오 알림톡 템플릿 수정에 실패했습니다.")
    }

    /**
     * 카카오 알림톡 템플릿 이름 수정 메소드
     * 알림톡 템플릿의 이름은 다른 템플릿과 중복하여 사용할 수 있고, 승인완료 된 템플릿도 수정할 수 있습니다.
     */
    @Throws
    fun updateKakaoAlimtalkTemplateName(
        templateId: String,
        name: String
    ): KakaoAlimtalkTemplateResponse {
        val response = this.messageHttpService.updateKakaoAlimtalkTemplateName(
            templateId,
            KakaoAlimtalkTemplateUpdateNameRequest(name)
        ).execute()
        return handleApiResponse(response, "카카오 알림톡 템플릿의 이름 수정에 실패했습니다.")
    }

    /**
     * 카카오 알림톡 템플릿 검수 요청 메소드
     */
    @Throws
    fun requestKakaoAlimtalkTemplateInspection(templateId: String): KakaoAlimtalkTemplateResponse {
        val response =
            this.messageHttpService.requestKakaoAlimtalkTemplateInspection(templateId).execute()
        return handleApiResponse(response, "카카오 알림톡 템플릿 검수 요청에 실패했습니다.")
    }

    /**
     * 카카오 알림톡 템플릿 검수 취소 메소드
     */
    @Throws
    fun cancelKakaoAlimtalkTemplateInspection(templateId: String): KakaoAlimtalkTemplateResponse {
        val response =
            this.messageHttpService.cancelKakaoAlimtalkTemplateInspection(templateId).execute()
        return handleApiResponse(response, "카카오 알림톡 템플릿 검수 취소에 실패했습니다.")
    }

    /**
     * 카카오 알림톡 템플릿 삭제 메소드
     */
    @Throws
    fun removeKakaoAlimtalkTemplate(templateId: String): KakaoAlimtalkTemplateResponse {
        val response = this.messageHttpService.removeKakaoAlimtalkTemplate(templateId).execute()
        return handleApiResponse(response, "카카오 알림톡 템플릿 삭제에 실패했습니다.")
    }

    /**
     * 카카오 브랜드 메시지 템플릿 조회 메소드
     */
    @Throws
    @JvmOverloads
    fun getKakaoBrandMessageTemplates(parameter: KakaoBrandMessageTemplateListRequest? = null): KakaoBrandMessageTemplateListResponse {
        val queryParams = parameter?.generateQueryParams() ?: emptyMap()
        val response = this.messageHttpService.getKakaoBrandMessageTemplates(queryParams).execute()
        return handleApiResponse(response, "카카오 브랜드 메시지 템플릿 조회에 실패했습니다.")
    }
}
