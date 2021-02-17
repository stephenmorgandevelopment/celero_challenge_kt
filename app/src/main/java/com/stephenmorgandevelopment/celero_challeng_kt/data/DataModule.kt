package com.stephenmorgandevelopment.celero_challeng_kt.data

import android.content.Context
import androidx.room.Room
import com.stephenmorgandevelopment.celero_challeng_kt.doas.ClientDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideClientDatabase(@ApplicationContext context: Context): ClientDatabase =
        Room.databaseBuilder(context, ClientDatabase::class.java, "client.db")
            .build()

    @Provides
    @Singleton
    fun provideClientDao(clientDatabase: ClientDatabase): ClientDao =
        clientDatabase.clientDao()

}