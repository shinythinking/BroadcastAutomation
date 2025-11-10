package com.shinythinking.broadcastautomation.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "template_fields",
    foreignKeys = [
        ForeignKey(
            entity = TemplateEntity::class,
            parentColumns = ["id"],
            childColumns = ["templateId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("templateId")]
)
data class TemplateFieldEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val templateId: String,
    val fieldId: String,
    val fieldName: String,
    val fieldType: String,
    val placeholder: String = "",
    val isRequired: Boolean = true,
    val order: Int
)
