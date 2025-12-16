package com.shinythinking.broadcastautomation.domain.model

data class BroadcastMessage(
    val text: String,
    val voiceSettings: VoiceSettings,
    val schedule: BroadcastSchedule
)

data class VoiceSettings(
    val voiceGender: VoiceGender = VoiceGender.FEMALE,
    val speed: VoiceSpeed = VoiceSpeed.NORMAL,
    val volume: VoiceVolume = VoiceVolume.NORMAL
)

enum class VoiceGender(val displayName: String) {
    MALE("남성 음성"),
    FEMALE("여성 음성")
}

enum class VoiceSpeed(val displayName: String, val value: Int) {
    VERY_SLOW("매우 느리게", 70),
    SLOW("느리게", 85),
    NORMAL("보통 속도", 100),
    FAST("빠르게", 120),
    VERY_FAST("매우 빠르게", 150)
}

enum class VoiceVolume(val displayName: String, val value: Int) {
    QUIET("조용히", 80),
    NORMAL("보통 목소리", 100),
    LOUD("크게", 120),
    VERY_LOUD("매우 크게", 150)
}
