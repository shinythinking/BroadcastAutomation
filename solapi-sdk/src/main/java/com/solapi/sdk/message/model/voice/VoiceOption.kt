package com.solapi.sdk.message.model.voice

import kotlinx.serialization.Serializable

@Serializable
data class VoiceOption(
    var voiceType: VoiceType = VoiceType.FEMALE,
    var headerMessage: String? = null,
    var tailMessage: String? = null,
    var replyRange: Int? = null,
    var counselorNumber: String? = null
)