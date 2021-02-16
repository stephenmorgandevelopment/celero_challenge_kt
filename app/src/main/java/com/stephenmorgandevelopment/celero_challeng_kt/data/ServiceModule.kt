package com.stephenmorgandevelopment.celero_challeng_kt.data

import android.content.Context
import android.net.ConnectivityManager
import com.stephenmorgandevelopment.celero_challeng_kt.api.ClientService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideClientService(): ClientService {
        val retrofitClient = Retrofit.Builder()
            .baseUrl("https://hulet.tech")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofitClient.create(ClientService::class.java)
    }

    @Provides
    @Singleton
    fun provideConnectivityManager(
        @ApplicationContext context: Context
    ): ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}