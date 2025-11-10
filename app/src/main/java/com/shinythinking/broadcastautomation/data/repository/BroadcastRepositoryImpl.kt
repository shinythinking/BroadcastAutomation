package com.shinythinking.broadcastautomation.data.repository

import com.shinythinking.broadcastautomation.data.local.dao.ScriptDao
import com.shinythinking.broadcastautomation.data.local.dao.TemplateDao
import com.shinythinking.broadcastautomation.data.mapper.toDomain
import com.shinythinking.broadcastautomation.data.mapper.toEntity
import com.shinythinking.broadcastautomation.domain.model.Script
import com.shinythinking.broadcastautomation.domain.model.Template
import com.shinythinking.broadcastautomation.domain.repository.BroadcastRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BroadcastRepositoryImpl @Inject constructor(
    private val templateDao: TemplateDao,
    private val scriptDao: ScriptDao
) : BroadcastRepository {

    override fun getTemplatesFlow(): Flow<List<Template>> {
        return templateDao.getAllTemplatesWithFields()
            .map { templates -> templates.map { it.toDomain() } }
    }

    override suspend fun getTemplate(templateId: String): Template? {
        return templateDao.getTemplateWithFields(templateId)?.toDomain()
    }

    override suspend fun saveTemplate(template: Template) {
        val templateEntity = template.toEntity()
        val fieldEntities = template.fields.mapIndexed { index, field ->
            field.toEntity(template.id, index)
        }
        templateDao.insertTemplateWithFields(templateEntity, fieldEntities)
    }

    override suspend fun deleteTemplate(templateId: String) {
        templateDao.deleteTemplateById(templateId)
    }

    override fun getScriptsFlow(): Flow<List<Script>> {
        return scriptDao.getScripts()
            .map { scripts -> scripts.map { it.toDomain() } }
    }

    override suspend fun getScript(scriptId: String): Script? {
        return scriptDao.getScript(scriptId = scriptId)?.toDomain()
    }

    override fun searchScripts(query: String): Flow<List<Script>> {
        return scriptDao.searchScripts(query)
            .map { scripts -> scripts.map { it.toDomain() } }
    }

    override suspend fun saveScript(script: Script): String {
        val scriptEntity = script.toEntity()
        scriptDao.insertScript(scriptEntity)
        return script.id
    }

    override suspend fun deleteScript(scriptId: String) {
        scriptDao.getScript(scriptId)?.let { script ->
            scriptDao.deleteScript(script)
        }
    }

    override suspend fun updateFavoriteStatus(scriptId: String, isFavorite: Boolean) {
        scriptDao.updateFavoriteStatus(scriptId, isFavorite)
    }
}