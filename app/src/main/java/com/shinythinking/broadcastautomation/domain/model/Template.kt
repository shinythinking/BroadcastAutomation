package com.shinythinking.broadcastautomation.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Template(
    val id: String,
    val name: String,
    val icon: String,
    val template: String,
    val fields: List<TemplateField>
)

@Immutable
data class TemplateField(
    val id: String,
    val name: String,
    val type: FieldType,
    val placeholder: String = ""
)

enum class FieldType {
    DATE, TIME, TEXT, LOCATION
}