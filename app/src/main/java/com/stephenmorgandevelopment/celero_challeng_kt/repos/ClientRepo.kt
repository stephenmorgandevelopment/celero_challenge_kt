package com.stephenmorgandevelopment.celero_challeng_kt.repos

import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.lifecycle.LiveData
import com.stephenmorgandevelopment.celero_challeng_kt.api.ClientResponse
import com.stephenmorgandevelopment.celero_challeng_kt.doas.ClientDao
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import com.stephenmorgandevelopment.celero_challeng_kt.models.SimpleClient
import com.stephenmorgandevelopment.celero_challeng_kt.api.ClientService
import com.stephenmorgandevelopment.celero_challeng_kt.utils.Resource
import com.stephenmorgandevelopment.celero_challeng_kt.utils.Status
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import javax.annotation.Nullable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientRepo @Inject constructor(
    val connectivityManager: ConnectivityManager,
    val clientService: ClientService,
    val clientDao: ClientDao
) : DefaultClientRepo {
    private var lastFetchSaved: Long = -1

    // Variable added for ability to host multiple lists for multiple operators.
    private var jsonFileNameOnServer: String = "celerocustomers.json"

    override fun getClient(identifier: Long): Client {
        return clientDao.load(identifier)
    }

    override fun getLiveClient(identifier: Long): LiveData<Client> {
        return clientDao.loadLive(identifier)
    }

    override suspend fun getAll(
        forceUpdate: Boolean,
        list: String?
    ): LiveData<List<SimpleClient>> {

        if (!list.isNullOrEmpty()) {
            jsonFileNameOnServer = list
        }

        if (forceUpdate || needsRefreshed()) {
            refreshList()
        }

        return clientDao.loadSimpleClientsAsLiveData()
    }

    suspend fun getAllAsClientResponse(
        forceUpdate: Boolean,
        list: String?
    ): LiveData<ClientResponse> {

        if (!list.isNullOrEmpty()) {
            jsonFileNameOnServer = list
        }

        if (forceUpdate || needsRefreshed()) {
            processClientResponse(updateList())
        }

        return clientDao.loadSimpleClientsAsLiveDataClientResponse()
    }

    private suspend fun processClientResponse(resource: Resource<ClientResponse>): String {
        if (resource.status == Status.SUCCESS) {
            clientDao.updateDatabase(resource.data!!.clients!!)
            lastFetchSaved = System.currentTimeMillis()
            return "Success"
        } else if (resource.status == Status.ERROR) {
            val error = resource.message
            Log.d("ClientRepo", error.toString())
            return error!!
        } else {
            return "Loading"
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun updateList(): Resource<ClientResponse> {
        return if (isConnected()) {
            lateinit var resource: Resource<ClientResponse>
            withContext(Dispatchers.IO) {
                val response =
                    clientService.getAllClientsAsResponse(jsonFileNameOnServer)
                resource = if (response.isSuccessful && response.body() != null) {
                    Resource.success(response.body())
                } else {
                    Resource.error("An unknown error occured. Please try again.", null)
                }
            }

            return resource
        } else {
            Resource.error("No internet - Please check your connection.", null)
        }
//        try {
//            val response =
//                clientService.getAllClientsAsResponse(jsonFileNameOnServer)
//
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    return@let Resource.success(it)
//                }
//            //:? return Resource.error("An unknown error occured. Please try again.", null)
//            } else {
//                Resource.error("An unknown error occured. Please try again.", null)
//            }
//        } catch (e: Exception) {
//            Resource.error("Server error.  Please try again later.", null)
//        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun refreshList() {
        if (isConnected()) {
            withContext(Dispatchers.IO) {
                val response =
                    clientService.getAllClients(jsonFileNameOnServer).execute()

                processResponse(response)
            }
        }
    }

    private suspend fun processResponse(response: Response<List<Client>>) {
        if (response.isSuccessful) {
            clientDao.updateDatabase(response.body()!!)
            lastFetchSaved = System.currentTimeMillis()
        } else {
            val error = response.errorBody()
            Log.d("ClientRepo", error.toString())
        }
    }

    private fun needsRefreshed(
        timeout: Long = 300000
    ): Boolean {
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