package com.shinythinking.broadcastautomation.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class AIGenerationOptions(
    val tone: AITone = AITone.NORMAL,
    val length: AILength = AILength.SHORT
) {
    fun toPrompt(): String {
        return "문장을 ${tone.value} ${length.description} 분량으로 작성해주세요."
    }
}

enum class AITone(val displayName: String, val value: String) {
    NORMAL("일반적으로", "일반적인 톤으로"),
    URGENT("긴급하게", "긴급한 톤으로"),
    SOFT("부드럽게", "부드러운 톤으로"),
    FORMAL("격식있게", "격식있는 톤으로"),
    Wit("재치 있게", "재치 있는 톤으로")
}

enum class AILength(val displayName: String, val description: String) {
    SHORT("간결하게", "30초"),
    MEDIUM("보통", "1분"),
    LONG("상세하게", "2분")
}