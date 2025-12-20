package com.shinythinking.broadcastautomation.data.repository

import com.shinythinking.broadcastautomation.data.model.Content
import com.shinythinking.broadcastautomation.data.model.GeminiRequest
import com.shinythinking.broadcastautomation.data.model.Part
import com.shinythinking.broadcastautomation.data.remote.GeminiDataSource
import com.shinythinking.broadcastautomation.domain.repository.LLMRepository
import javax.inject.Inject

class LLMRepositoryImpl @Inject constructor(
    private val apiKey: String,
    private val geminiDataSource: GeminiDataSource,
) : LLMRepository {
    override suspend fun generateScript(fullPrompt: String): Result<String> {
        return try {
            val request = GeminiRequest(
                contents = listOf(Content(parts = listOf(Part(text = fullPrompt))))
            )

            val response = geminiDataSource.generateContent(apiKey, request)

            val generatedText = response.candidates?.firstOrNull()
                ?.content?.parts?.firstOrNull()?.text
                ?: return Result.failure(Exception("No content generated"))

            Result.success(generatedText)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}