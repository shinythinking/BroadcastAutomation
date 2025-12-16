package com.shinythinking.broadcastautomation.domain.usecase

import com.shinythinking.broadcastautomation.domain.model.Script
import com.shinythinking.broadcastautomation.domain.repository.LocalDataRepository
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

data class NoneTemplate(
    val title: String,
    val content: String
)

class GenerateScriptFromNoneUseCase @Inject constructor(
    private val repository: LocalDataRepository
) {
    suspend operator fun invoke(
        noneTemplate: NoneTemplate,
    ): Result<Script> {
        return try {
            val script = Script(
                id = UUID.randomUUID().toString(),
                title = noneTemplate.title,
                content = noneTemplate.content,
                createdAt = LocalDateTime.now(),
                templateId = "NONE",
            )

            repository.saveScript(script)

            Result.success(script)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}