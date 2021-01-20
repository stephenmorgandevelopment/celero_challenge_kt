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
import javax.annotation.Nullable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientRepo @Inject constructor(
    val connectivityManager: ConnectivityManager,
    val clientService: ClientService?,
    val clientDao: ClientDao
) {
    private var lastFetchSaved: Long = -1

    // Variable added for ability to host multiple lists for multiple operators.
    private var jsonFileNameOnServer: String = "celerocustomers.json"

    fun getClient(identifier: Long): Client {
        return clientDao.load(identifier)
    }

    fun getLiveClient(identifier: Long): LiveData<Client> {
        return clientDao.loadLive(identifier)
    }

    suspend fun getAll(
        forceUpdate: Boolean = false,
        list: String? = null
    ): LiveData<List<SimpleClient>> {

        if(!list.isNullOrEmpty()) {
            jsonFileNameOnServer = list
        }

        if(forceUpdate) {
            lastFetchSaved = -1
        }

        if(clientService != null) {
            refreshList()
        }

        return clientDao.loadSimpleClientsAsLiveData()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun refreshList() {
        if (needsRefreshed() && isConnected()) {
            withContext(Dispatchers.IO) {
                val response =
                    clientService!!.getAllClients(jsonFileNameOnServer).execute()

                processResponse(response)
                lastFetchSaved = System.currentTimeMillis()
            }
        }
    }

    private suspend fun processResponse(response: Response<List<Client>>) {
        if (response.isSuccessful) {
            clientDao.updateDatabase(response.body()!!)
        } else {
            val error = response.errorBody()
            Log.d("ClientRepo", error.toString())
        }
    }

    private fun needsRefreshed(): Boolean {
        val timeout: Long = 300000
        return ((System.currentTimeMillis() - lastFetchSaved) > timeout)
    }

    private fun setJsonFileNameOnServer(jsonFileNameOnServer: String) {
        this.jsonFileNameOnServer = jsonFileNameOnServer
    }

    private fun isConnected(): Boolean {
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    // Repo implementation of test to check proper list updating.
    // May look at moving this to where it belongs.  In the test folders.
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun getTestList(): LiveData<List<SimpleClient>> {
        if (isConnected()) {
            withContext(Dispatchers.IO) {
                processResponse(fetchTestList().execute())
            }
        }

        return clientDao.loadSimpleClientsAsLiveData()
    }

    fun fetchTestList(): Call<List<Client>> {
        setJsonFileNameOnServer("clients.json")
        return getTestRetrofitClient().getTestClients(jsonFileNameOnServer)
    }

    fun getTestRetrofitClient(): ClientService {
        return Retrofit.Builder()
            .baseUrl("https://www.stephenmorgan-portfolio.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClientService::class.java)
    }
}