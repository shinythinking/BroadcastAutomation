package com.shinythinking.broadcastautomation.data.local

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.shinythinking.broadcastautomation.data.local.dao.TemplateDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class DatabaseInitializer @Inject constructor(
    private val templateDaoProvider: Provider<TemplateDao>
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        CoroutineScope(Dispatchers.IO).launch {
            val templateDao = templateDaoProvider.get()

            val count = templateDao.getPreinstalledTemplateCount()
            if (count == 0) {
                Preinstalled.getTemplates().forEach { (template, fields) ->
                    templateDao.insertTemplateWithFields(template, fields)
                }
            }
        }
    }
}
