package com.dev_musashi.chat.module

import android.content.Context
import com.dev_musashi.ranchat.data.Api
import com.dev_musashi.ranchat.data.RepositoryImpl
import com.dev_musashi.ranchat.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(
        api: Api,
        @ApplicationContext context: Context
    ) : Repository {
        return RepositoryImpl(api, context)
    }
}