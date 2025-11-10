package com.shinythinking.broadcastautomation.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TemplateWithFields(
    @Embedded val template: TemplateEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "templateId"
    )
    val fields: List<TemplateFieldEntity>
)