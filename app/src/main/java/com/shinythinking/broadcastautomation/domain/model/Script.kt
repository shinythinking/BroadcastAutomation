package com.shinythinking.broadcastautomation.domain.model

import androidx.compose.runtime.Immutable
import java.time.LocalDateTime

@Immutable
data class Script(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val metadata: Map<String, String> = emptyMap()
)
