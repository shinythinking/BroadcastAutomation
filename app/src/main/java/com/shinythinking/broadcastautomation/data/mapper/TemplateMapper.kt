package com.shinythinking.broadcastautomation.data.mapper

import com.shinythinking.broadcastautomation.data.local.entity.TemplateEntity
import com.shinythinking.broadcastautomation.data.local.entity.TemplateFieldEntity
import com.shinythinking.broadcastautomation.data.local.entity.TemplateWithFields
import com.shinythinking.broadcastautomation.domain.model.FieldType
import com.shinythinking.broadcastautomation.domain.model.Template
import com.shinythinking.broadcastautomation.domain.model.TemplateField

fun TemplateWithFields.toDomain(): Template {
    return Template(
        id = template.id,
        name = template.name,
        icon = template.icon,
        template = template.template,
        fields = fields.map { it.toDomain() }
    )
}

fun TemplateFieldEntity.toDomain(): TemplateField {
    return TemplateField(
        id = fieldId,
        name = fieldName,
        type = FieldType.valueOf(fieldType),
        placeholder = placeholder
    )
}

fun Template.toEntity(): TemplateEntity {
    return TemplateEntity(
        id = id,
        name = name,
        icon = icon,
        template = template,
    )
}

fun TemplateField.toEntity(templateId: String, order: Int): TemplateFieldEntity {
    return TemplateFieldEntity(
        templateId = templateId,
        fieldId = id,
        fieldName = name,
        fieldType = type.name,
        placeholder = placeholder,
        order = order
    )
}
