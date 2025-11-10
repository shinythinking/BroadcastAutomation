package com.shinythinking.broadcastautomation.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shinythinking.broadcastautomation.data.local.dao.ScriptDao
import com.shinythinking.broadcastautomation.data.local.dao.TemplateDao
import com.shinythinking.broadcastautomation.data.local.entity.ScriptEntity
import com.shinythinking.broadcastautomation.data.local.entity.TemplateEntity
import com.shinythinking.broadcastautomation.data.local.entity.TemplateFieldEntity

@Database(
    entities = [
        TemplateEntity::class,
        TemplateFieldEntity::class,
        ScriptEntity::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class BroadcastDatabase : RoomDatabase() {
    abstract fun templateDao(): TemplateDao
    abstract fun scriptDao(): ScriptDao

    companion object {
        const val DATABASE_NAME = "broadcast_database"
    }
}