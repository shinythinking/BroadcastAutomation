package com.solapi.sdk.message.lib

import com.solapi.sdk.message.dto.request.MessageListBaseRequest

/**
 * Helper function to add parameter to criteria, condition, and value strings
 */
fun addMessageListParameterToCriteria(
    payload: MessageListBaseRequest,
    fieldName: String,
    parameterValue: String?
) {
    parameterValue?.takeIf { it.isNotBlank() }?.let { value ->
        payload.criteria =
            if (payload.criteria.isNullOrBlank()) fieldName else "${payload.criteria},$fieldName"
        payload.cond = if (payload.cond.isNullOrBlank()) "eq" else "${payload.cond},eq"
        payload.value = if (payload.value.isNullOrBlank()) value else "${payload.value},$value"
    }
}
