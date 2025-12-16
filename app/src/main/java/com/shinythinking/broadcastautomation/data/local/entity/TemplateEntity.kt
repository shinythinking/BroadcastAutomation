package com.shinythinking.broadcastautomation.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "templates")
data class TemplateEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val icon: String,
    val template: String,
    val order: Int = 0,
    val isPreinstalled: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)