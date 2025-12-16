package com.shinythinking.broadcastautomation.data.remote

import com.shinythinking.broadcastautomation.BuildConfig
import com.shinythinking.broadcastautomation.data.model.MessageDto
import com.solapi.sdk.message.dto.request.SendRequestConfig
import com.solapi.sdk.message.exception.SolapiMessageNotReceivedException
import com.solapi.sdk.message.model.Message
import com.solapi.sdk.message.model.voice.VoiceOption
import com.solapi.sdk.message.service.DefaultMessageService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SolapiDataSource @Inject constructor(
    private val messageService: DefaultMessageService
) {
    fun sendMessage(message: MessageDto): Result<Unit> {
        val voiceOption = VoiceOption(
            voiceType = message.gender,
        )
        val taggedText =
            """${message.headerMessage} <volume="200"><speed="50">${message.text}</speed></volume>"""

        val broadcastMessage = Message(
            text = taggedText,
            to = BuildConfig.PHONE_TO,
            from = BuildConfig.PHONE_FROM,
            voiceOptions = voiceOption,
        )

        return try {
            val config = SendRequestConfig(scheduledDate = message.scheduledTime)

            messageService.send(broadcastMessage, config)
            Result.success(Unit)

        } catch (exception: SolapiMessageNotReceivedException) {
            Result.failure(
                Exception(
                    "메시지 전송 실패: ${exception.message}/n${exception.failedMessageList}",
                    exception
                )
            )
        } catch (exception: Exception) {
            Result.failure(
                Exception("예상치 못한 오류: ${exception.message}", exception)
            )
        }
    }
}