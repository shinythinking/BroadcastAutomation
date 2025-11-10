package com.shinythinking.broadcastautomation.data.mapper

import com.shinythinking.broadcastautomation.data.local.entity.ScriptEntity
import com.shinythinking.broadcastautomation.domain.model.Script
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun ScriptEntity.toDomain(): Script {
    return Script(
        id = id,
        title = title,
        content = content,
        createdAt = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(createdAt),
            ZoneId.systemDefault()
        ),
        templateId = templateId,
    )
}

fun Script.toEntity(generationMethod: String = "UNKNOWN"): ScriptEntity {
    return ScriptEntity(
        id = id,
        title = title,
        content = content,
        templateId = templateId,
        createdAt = createdAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )
}