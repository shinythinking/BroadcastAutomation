package com.shinythinking.broadcastautomation.domain.usecase

import com.shinythinking.broadcastautomation.domain.model.Script
import com.shinythinking.broadcastautomation.domain.repository.LocalDataRepository
import javax.inject.Inject

class UpdateScriptUseCase @Inject constructor(
    private val repository: LocalDataRepository
) {
    suspend operator fun invoke(script: Script): Result<String> {
        return try {
            if (script.content.isBlank()) {
                return Result.failure(
                    IllegalArgumentException("대본 내용을 입력해주세요")
                )
            }

            repository.saveScript(script)
            Result.success(script.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}