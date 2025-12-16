package com.shinythinking.broadcastautomation.data.mapper

import com.shinythinking.broadcastautomation.data.model.MessageDto
import com.shinythinking.broadcastautomation.domain.model.BroadcastMessage
import com.shinythinking.broadcastautomation.domain.model.BroadcastSchedule
import com.shinythinking.broadcastautomation.domain.model.VoiceGender
import com.solapi.sdk.message.model.voice.VoiceType.FEMALE
import com.solapi.sdk.message.model.voice.VoiceType.MALE
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun BroadcastMessage.toDto(): MessageDto {
    return MessageDto(
        text = text,
        gender = when (voiceSettings.voiceGender) {
            VoiceGender.MALE -> MALE
            VoiceGender.FEMALE -> FEMALE
        },
        speed = voiceSettings.speed.value,
        volume = voiceSettings.volume.value,
        scheduledTime = when (schedule) {
            is BroadcastSchedule.Immediate -> null
            is BroadcastSchedule.Scheduled -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val localDateTime = LocalDateTime.parse(schedule.scheduledTime, formatter)

                val zoneOffset = ZoneId.systemDefault().rules.getOffset(localDateTime)
                localDateTime.toInstant(zoneOffset)
            }
        }
    )
}
