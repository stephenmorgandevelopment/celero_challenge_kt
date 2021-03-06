package com.stephenmorgandevelopment.celero_challeng_kt.repos

import android.net.ConnectivityManager
import com.stephenmorgandevelopment.celero_challeng_kt.api.ClientService
import com.stephenmorgandevelopment.celero_challeng_kt.doas.ClientDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Provides
    @Singleton
    fun provideClientRepo(
        connectivityManager: ConnectivityManager,
        clientService: ClientService?,
        clientDao: ClientDao
    ): DefaultClientRepo = ClientRepo(
        connectivityManager,
        clientService,
        clientDao
    ) as DefaultClientRepo

}