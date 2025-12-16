package com.shinythinking.broadcastautomation.di

import com.shinythinking.broadcastautomation.BuildConfig
import com.shinythinking.broadcastautomation.data.remote.SolapiDataSource
import com.shinythinking.broadcastautomation.data.repository.BroadcastRepositoryImpl
import com.shinythinking.broadcastautomation.domain.repository.BroadcastRepository
import com.solapi.sdk.SolapiClient
import com.solapi.sdk.message.service.DefaultMessageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideMessageService(): DefaultMessageService {
        return SolapiClient.createInstance(
            apiKey = BuildConfig.API_KEY,
            apiSecretKey = BuildConfig.API_SECRET_KEY
        )
    }

    @Provides
    @Singleton
    fun provideSolapiDataSource(
        solapiClient: DefaultMessageService
    ): SolapiDataSource {
        return SolapiDataSource(solapiClient)
    }

    @Provides
    @Singleton
    fun provideMessageRepository(
        solapiDataSource: SolapiDataSource
    ): BroadcastRepository {
        return BroadcastRepositoryImpl(solapiDataSource)
    }
}
