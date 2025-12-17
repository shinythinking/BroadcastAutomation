package com.shinythinking.broadcastautomation.di

import android.content.Context
import androidx.room.Room
import com.shinythinking.broadcastautomation.data.local.BroadcastDatabase
import com.shinythinking.broadcastautomation.data.local.DatabaseInitializer
import com.shinythinking.broadcastautomation.data.local.dao.ScriptDao
import com.shinythinking.broadcastautomation.data.local.dao.TemplateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabaseInitializer(
        templateDaoProvider: Provider<TemplateDao>
    ): DatabaseInitializer {
        return DatabaseInitializer(templateDaoProvider)
    }

    @Provides
    @Singleton
    fun provideBroadcastDatabase(
        @ApplicationContext context: Context,
        databaseInitializer: DatabaseInitializer
    ): BroadcastDatabase {
        return Room.databaseBuilder(
            context,
            BroadcastDatabase::class.java,
            BroadcastDatabase.DATABASE_NAME
        )
            .addCallback(databaseInitializer)
            .build()
    }

    @Provides
    @Singleton
    fun provideTemplateDao(database: BroadcastDatabase): TemplateDao {
        return database.templateDao()
    }

    @Provides
    @Singleton
    fun provideScriptDao(database: BroadcastDatabase): ScriptDao {
        return database.scriptDao()
    }
}
