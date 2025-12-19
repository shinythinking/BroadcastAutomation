package com.shinythinking.broadcastautomation.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class AIGenerationOptions(
    val tone: AITone = AITone.NORMAL,
    val length: AILength = AILength.SHORT
)

enum class AITone(val displayName: String) {
    NORMAL("일반적으로"),
    URGENT("긴급하게"),
    SOFT("부드럽게"),
    FORMAL("격식있게")
}

enum class AILength(val displayName: String, val description: String) {
    SHORT("간결하게", "30초"),
    MEDIUM("보통", "1분"),
    LONG("상세하게", "2분")
}