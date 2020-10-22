package com.stephenmorgandevelopment.celero_challeng_kt.repos

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.lifecycle.LiveData
import com.stephenmorgandevelopment.celero_challeng_kt.doas.ClientDao
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import com.stephenmorgandevelopment.celero_challeng_kt.models.SimpleClient
import com.stephenmorgandevelopment.celero_challeng_kt.api.ClientService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientRepo @Inject constructor(
    @ApplicationContext val context: Context,
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

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun refreshList(list: String) {
        val timeout: Long = 300000
        val needsRefreshed = ((System.currentTimeMillis() - lastFetchSaved) > timeout)

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if (needsRefreshed && isConnected) {
            withContext(Dispatchers.IO) {
                val response = clientService.getAllClients(list).execute()

                if (response.isSuccessful && response.body() != null && response.body().toString()
                        .isNotEmpty()
                ) {
                    clientDao.deleteAll()
                    clientDao.insertAll(response.body()!!)
                    lastFetchSaved = System.currentTimeMillis()
                } else {
                    val error = response.errorBody()
                    Log.d("ClientRepo", error.toString())
                    TODO("Report error to user.")
                }
            }
        }
    }
}