package com.shinythinking.broadcastautomation.domain.repository

interface LLMRepository {
    suspend fun generateScript(fullPrompt: String): Result<String>
}