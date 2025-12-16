package com.shinythinking.broadcastautomation.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.shinythinking.broadcastautomation.data.local.entity.TemplateEntity
import com.shinythinking.broadcastautomation.data.local.entity.TemplateFieldEntity
import com.shinythinking.broadcastautomation.data.local.entity.TemplateWithFields
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateDao {
    @Transaction
    @Query("SELECT * FROM templates ORDER BY `order` ASC, name ASC")
    fun getAllTemplatesWithFields(): Flow<List<TemplateWithFields>>

    @Transaction
    @Query("SELECT * FROM templates WHERE id = :templateId")
    suspend fun getTemplateWithFields(templateId: String): TemplateWithFields?

    @Query("SELECT * FROM templates WHERE id = :templateId")
    suspend fun getTemplate(templateId: String): TemplateEntity?

    @Query("SELECT * FROM template_fields WHERE templateId = :templateId ORDER BY `order` ASC")
    suspend fun getFieldsForTemplate(templateId: String): List<TemplateFieldEntity>

    @Query("SELECT COUNT(*) FROM templates WHERE isPreinstalled = 1")
    suspend fun getPreinstalledTemplateCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplate(template: TemplateEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFields(fields: List<TemplateFieldEntity>)

    @Transaction
    suspend fun insertTemplateWithFields(
        template: TemplateEntity,
        fields: List<TemplateFieldEntity>
    ) {
        insertTemplate(template)
        insertFields(fields)
    }

    @Delete
    suspend fun deleteTemplate(template: TemplateEntity)

    @Query("DELETE FROM templates WHERE id = :templateId")
    suspend fun deleteTemplateById(templateId: String)

    @Update
    suspend fun updateTemplate(template: TemplateEntity)
}