package com.shinythinking.broadcastautomation.domain.usecase

import com.shinythinking.broadcastautomation.domain.model.AIGenerationOptions
import com.shinythinking.broadcastautomation.domain.model.Script
import com.shinythinking.broadcastautomation.domain.repository.LocalDataRepository
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class GenerateScriptFromAIUseCase @Inject constructor(
    private val repository: LocalDataRepository
) {
    suspend operator fun invoke(
        keywords: String,
        options: AIGenerationOptions
    ): Result<Script> {
        return try {
            if (keywords.isBlank()) {
                return Result.failure(
                    IllegalArgumentException("키워드를 입력해주세요")
                )
            }

            val generatedContent = repository.generateScriptWithAI(keywords, options)

            val script = Script(
                id = UUID.randomUUID().toString(),
                title = "AI 생성 방송",
                content = generatedContent,
                createdAt = LocalDateTime.now(),
                templateId = "AI"
            )

            repository.saveScript(script)

            Result.success(script)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}