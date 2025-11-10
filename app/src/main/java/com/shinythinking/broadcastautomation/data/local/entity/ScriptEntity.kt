package com.shinythinking.broadcastautomation.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scripts")
data class ScriptEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val templateId: String,
    val createdAt: Long,
    val updatedAt: Long = createdAt,
    val isFavorite: Boolean = false
)