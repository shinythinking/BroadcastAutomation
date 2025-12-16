package com.shinythinking.broadcastautomation.domain.usecase

import com.shinythinking.broadcastautomation.domain.model.VoiceSettings
import com.shinythinking.broadcastautomation.domain.repository.LocalDataRepository
import javax.inject.Inject

class GenerateVoiceUseCase @Inject constructor(
    private val repository: LocalDataRepository
) {
    suspend operator fun invoke(
        scriptId: String,
        settings: VoiceSettings
    ): Result<Unit> {
        return try {
            repository.generateVoice(scriptId, settings)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}