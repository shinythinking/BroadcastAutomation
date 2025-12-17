package com.shinythinking.broadcastautomation.di

import com.shinythinking.broadcastautomation.data.local.dao.ScriptDao
import com.shinythinking.broadcastautomation.data.local.dao.TemplateDao
import com.shinythinking.broadcastautomation.data.repository.LocalDataRepositoryImpl
import com.shinythinking.broadcastautomation.domain.repository.LocalDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBroadcastRepository(
        templateDao: TemplateDao,
        scriptDao: ScriptDao
    ): LocalDataRepository {
        return LocalDataRepositoryImpl(templateDao, scriptDao)
    }


}