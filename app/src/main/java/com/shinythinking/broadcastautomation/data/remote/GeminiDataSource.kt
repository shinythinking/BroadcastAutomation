package com.shinythinking.broadcastautomation.data.remote

import com.shinythinking.broadcastautomation.data.model.GeminiRequest
import com.shinythinking.broadcastautomation.data.model.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiDataSource {
    @POST("v1beta/models/gemini-2.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}