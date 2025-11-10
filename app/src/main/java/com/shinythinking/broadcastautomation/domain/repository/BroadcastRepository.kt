package com.shinythinking.broadcastautomation.domain.repository

import com.shinythinking.broadcastautomation.domain.model.Script
import com.shinythinking.broadcastautomation.domain.model.Template
import kotlinx.coroutines.flow.Flow

interface BroadcastRepository {
    fun getTemplatesFlow(): Flow<List<Template>>
    suspend fun getTemplate(templateId: String): Template?
    suspend fun saveTemplate(template: Template)
    suspend fun deleteTemplate(templateId: String)

    fun getScriptsFlow(): Flow<List<Script>>
    suspend fun getScript(scriptId: String): Script?
    fun searchScripts(query: String): Flow<List<Script>>
    suspend fun saveScript(script: Script): String
    suspend fun deleteScript(scriptId: String)
    suspend fun updateFavoriteStatus(scriptId: String, isFavorite: Boolean)
}