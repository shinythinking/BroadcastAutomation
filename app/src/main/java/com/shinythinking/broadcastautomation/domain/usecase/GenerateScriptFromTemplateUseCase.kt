package com.shinythinking.broadcastautomation.domain.usecase

import com.shinythinking.broadcastautomation.domain.model.Script
import com.shinythinking.broadcastautomation.domain.model.Template
import com.shinythinking.broadcastautomation.domain.repository.LocalDataRepository
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class GenerateScriptFromTemplateUseCase @Inject constructor(
    private val repository: LocalDataRepository
) {
    suspend operator fun invoke(
        template: Template,
        fieldValues: Map<String, String>
    ): Result<Script> {
        return try {
            var content = template.template
            fieldValues.forEach { (fieldId, value) ->
                content = content.replace("[$fieldId]", value)
            }

            if (content.contains("[") && content.contains("]")) {
                return Result.failure(
                    IllegalArgumentException("모든 필수 항목을 입력해주세요")
                )
            }

            val script = Script(
                id = UUID.randomUUID().toString(),
                title = template.name,
                content = content,
                createdAt = LocalDateTime.now(),
                templateId = template.id,
            )

            repository.saveScript(script)

            Result.success(script)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}