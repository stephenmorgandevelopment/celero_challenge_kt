package com.stephenmorgandevelopment.celero_challeng_kt.data

import com.stephenmorgandevelopment.celero_challeng_kt.api.ClientService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideClientService(): ClientService {
        val retrofitClient: Retrofit = Retrofit.Builder()
            .baseUrl("https://hulet.tech")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofitClient.create(ClientService::class.java)
    }
}