package com.solapi.sdk.message.model.voice

enum class VoiceReplyRange(val value: Int) {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9);

    companion object {
        private val map = entries.associateBy { it.name.lowercase() }

        fun fromKey(key: String): VoiceReplyRange? = map[key]
    }
}