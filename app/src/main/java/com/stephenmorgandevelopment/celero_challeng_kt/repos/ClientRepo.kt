package com.stephenmorgandevelopment.celero_challeng_kt.repos

import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.lifecycle.LiveData
import com.stephenmorgandevelopment.celero_challeng_kt.doas.ClientDao
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import com.stephenmorgandevelopment.celero_challeng_kt.models.SimpleClient
import com.stephenmorgandevelopment.celero_challeng_kt.api.ClientService
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientRepo @Inject constructor(
    val connectivityManager: ConnectivityManager,
    val clientService: ClientService,
    val clientDao: ClientDao
) {
    private var lastFetchSaved: Long = -1

    fun getClient(identifier: Long): Client {
        return clientDao.load(identifier)
    }

    fun getLiveClient(identifier: Long): LiveData<Client> {
        return clientDao.loadLive(identifier)
    }

    suspend fun getAll(list: String): LiveData<List<SimpleClient>> {
        refreshList(list)
        return clientDao.loadSimpleClients()
    }

    // Repo implementation of test to check proper list updating.
    suspend fun getTestList(list: String): LiveData<List<SimpleClient>> { // : LiveData<List<SimpleClient>>
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if (isConnected) {
            @Suppress("BlockingMethodInNonBlockingContext")
            withContext(Dispatchers.IO) {
                val retrofitClient = Retrofit.Builder()
                    .baseUrl("https://www.stephenmorgan-portfolio.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofitClient.create(ClientService::class.java)

                val response = service.getTestClients(list).execute()

                if (response.isSuccessful) {
                    clientDao.deleteAll()
                    clientDao.insertAll(response.body()!!)
                } else {
                    val error = response.errorBody()
                    Log.d("ClientRepo", error.toString())
                }
            }
        }

        return clientDao.loadSimpleClients()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun refreshList(list: String) {
        val timeout: Long = 300000
        val needsRefreshed = ((System.currentTimeMillis() - lastFetchSaved) > timeout)

        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if (needsRefreshed && isConnected) {
            withContext(Dispatchers.IO) {
                val response = clientService.getAllClients(list).execute()

                if (response.isSuccessful) {
                    clientDao.deleteAll()
                    clientDao.insertAll(response.body()!!)
                    lastFetchSaved = System.currentTimeMillis()
                } else {
                    val error = response.errorBody()
                    Log.d("ClientRepo", error.toString())
                }
            }
        }
    }
}