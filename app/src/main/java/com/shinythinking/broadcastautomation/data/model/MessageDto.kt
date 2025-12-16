package com.shinythinking.broadcastautomation.data.model

import com.solapi.sdk.message.model.voice.VoiceType
import java.time.Instant

data class MessageDto(
    val headerMessage: String = """아<pause="5000">아<pause="10000">흠 흠 이전리 방송국입니다.""",
    val text: String,
    val gender: VoiceType,
    val speed: Int,
    val volume: Int,
    val scheduledTime: Instant? = null
)