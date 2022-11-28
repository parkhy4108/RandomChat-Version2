package com.dev_musashi.ranchat.module

import android.content.ContentResolver
import android.content.Context
import com.dev_musashi.ranchat.util.QueryImages
import com.dev_musashi.ranchat.util.SaveImage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {
    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context): QueryImages {
        return QueryImages(context)
    }

    @Singleton
    @Provides
    fun provideContext2(@ApplicationContext context: Context) : SaveImage {
        return SaveImage(context)
    }
}

