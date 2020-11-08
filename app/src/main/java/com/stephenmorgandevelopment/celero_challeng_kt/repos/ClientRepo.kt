package com.stephenmorgandevelopment.celero_challeng_kt.repos

import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.lifecycle.LiveData
import com.stephenmorgandevelopment.celero_challeng_kt.api.ClientApiResponse
import com.stephenmorgandevelopment.celero_challeng_kt.doas.ClientDao
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import com.stephenmorgandevelopment.celero_challeng_kt.models.SimpleClient
import com.stephenmorgandevelopment.celero_challeng_kt.api.ClientService
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
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
    private var jsonFileNameOnServer: String = "celerocustomers.json"

    fun setJsonFileNameOnServer(jsonFileNameOnServer: String) {
        this.jsonFileNameOnServer = jsonFileNameOnServer
    }

    fun getClient(identifier: Long): Client {
        return clientDao.load(identifier)
    }

    fun getLiveClient(identifier: Long): LiveData<Client> {
        return clientDao.loadLive(identifier)
    }

    suspend fun getAll(): LiveData<List<SimpleClient>> {
        refreshList(jsonFileNameOnServer)
        return clientDao.loadSimpleClients()
    }

    fun isConnected() : Boolean {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    // Repo implementation of test to check proper list updating.
    // May look at moving this to where it belongs.  In the test folders.
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun getTestList(): LiveData<List<SimpleClient>> {
        if (isConnected()) {
            withContext(Dispatchers.IO) {
//                val response = fetchTestList().execute()
                processResponse(fetchTestList().execute())
            }
        }

        return clientDao.loadSimpleClients()
    }

    private suspend fun processResponse(response: Response<List<Client>>) {
        if (response.isSuccessful) {
            clientDao.updateDatabase(response.body()!!)
        } else {
            val error = response.errorBody()
            Log.d("ClientRepo", error.toString())
        }
    }

//    private suspend fun updateDatabase(clients: List<Client>) {
//        clientDao.deleteAll()
//        clientDao.insertAll(clients)
//    }

    suspend fun getTestRetrofitClient() : ClientService {
        return Retrofit.Builder()
            .baseUrl("https://www.stephenmorgan-portfolio.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClientService::class.java)
    }

    suspend fun fetchTestList() : Call<List<Client>> {
        return getTestRetrofitClient().getTestClients("clients.json")
    }

    suspend fun needsRefreshed() : Boolean{
        val timeout: Long = 300000
        return ((System.currentTimeMillis() - lastFetchSaved) > timeout)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun refreshList(list: String) {
        if (needsRefreshed() && isConnected()) {
            withContext(Dispatchers.IO) {
                val response = clientService.getAllClients(jsonFileNameOnServer).execute()

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