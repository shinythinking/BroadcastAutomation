package com.solapi.sdk.message.lib

import com.solapi.sdk.message.dto.request.MultipleDetailMessageSendingRequest
import com.solapi.sdk.message.dto.response.ErrorResponse
import com.solapi.sdk.message.dto.response.MultipleDetailMessageSentResponse
import com.solapi.sdk.message.exception.SolapiBadRequestException
import com.solapi.sdk.message.exception.SolapiEmptyResponseException
import com.solapi.sdk.message.exception.SolapiInvalidApiKeyException
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException
import com.solapi.sdk.message.exception.SolapiUnknownException
import com.solapi.sdk.message.model.group.GroupCount
import com.solapi.sdk.message.service.MessageHttpService
import retrofit2.Response

/**
 * 메시지 발송 요청을 처리하는 공통 헬퍼 메소드
 */
@Throws(
    SolapiMessageNotReceivedException::class,
    SolapiEmptyResponseException::class,
    SolapiUnknownException::class
)
fun processSendRequest(
    messageHttpService: MessageHttpService,
    parameter: MultipleDetailMessageSendingRequest
): MultipleDetailMessageSentResponse {
    val response = messageHttpService.sendManyDetail(parameter).execute()

    if (response.isSuccessful) {
        val responseBody = response.body()
        if (responseBody != null) {
            val count: GroupCount = responseBody.groupInfo?.count ?: GroupCount()
            val failedMessageList = responseBody.failedMessageList

            if (failedMessageList.isNotEmpty() && (count.total ?: 0) == failedMessageList.count()) {
                val messageNotReceivedException =
                    SolapiMessageNotReceivedException("메시지 발송 접수에 실패했습니다.")
                messageNotReceivedException.failedMessageList = failedMessageList
                throw messageNotReceivedException
            }

            return responseBody
        }
        throw SolapiEmptyResponseException("서버로부터 아무 응답을 받지 못했습니다.")
    } else {
        val errorString = response.errorBody()?.string() ?: "Server error encountered"
        throw SolapiUnknownException(errorString)
    }
}

/**
 * 에러 응답을 처리하는 공통 헬퍼 메소드
 */
@Throws(
    SolapiBadRequestException::class,
    SolapiInvalidApiKeyException::class,
    SolapiUnknownException::class
)
fun handleErrorResponse(errorBody: String?): Nothing {
    val errorResponse: ErrorResponse = JsonSupport.json.decodeFromString(errorBody ?: "")
    when (errorResponse.errorCode) {
        "ValidationError" -> throw SolapiBadRequestException(errorResponse.errorMessage)
        "InvalidApiKey" -> throw SolapiInvalidApiKeyException(errorResponse.errorMessage)
        "FailedToAddMessage" -> throw SolapiBadRequestException(errorResponse.errorMessage)
        else -> throw SolapiUnknownException("${errorResponse.errorCode}: ${errorResponse.errorMessage}")
    }
}

/**
 * SOLAPI API 응답을 제어하는 공통 헬퍼 메소드
 */
@Throws
fun <T> handleApiResponse(response: Response<T>, errorMessage: String): T {
    if (response.isSuccessful) {
        return response.body() ?: throw SolapiUnknownException(errorMessage)
    } else {
        handleErrorResponse(response.errorBody()?.string())
    }
}
