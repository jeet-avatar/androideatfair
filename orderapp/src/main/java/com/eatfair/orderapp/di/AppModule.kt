package com.eatfair.orderapp.di

import android.content.Context
import com.eatfair.orderapp.data.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides a Singleton instance of SessionManager.
     * Hilt automatically injects the ApplicationContext, which is necessary for DataStore.
     */

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        // Hilt will automatically look up the application context and pass it here.
        return SessionManager(context)
    }

}