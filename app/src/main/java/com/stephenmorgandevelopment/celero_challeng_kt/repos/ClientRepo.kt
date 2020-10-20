package com.stephenmorgandevelopment.celero_challeng_kt.repos

import android.util.Log
import androidx.lifecycle.LiveData
import com.stephenmorgandevelopment.celero_challeng_kt.doas.ClientDao
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import com.stephenmorgandevelopment.celero_challeng_kt.models.SimpleClient
import com.stephenmorgandevelopment.celero_challeng_kt.api.ClientService
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientRepo @Inject constructor(
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

    suspend fun getAll(list: String, connected: Boolean): LiveData<List<SimpleClient>> {
        if(connected) {
            refreshList(list)
        }
        return clientDao.loadSimpleClients()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun refreshList(list: String) {
        val needsRefreshed = ((System.currentTimeMillis() - lastFetchSaved) > 3600000)

        if (needsRefreshed) {
            withContext(Dispatchers.IO) {
                val response = clientService.getAllClients(list).execute()

                if (response.isSuccessful && response.body() != null && response.body().toString().isNotEmpty()) {
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