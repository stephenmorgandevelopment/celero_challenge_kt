package com.stephenmorgandevelopment.celero_challeng_kt.repos

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stephenmorgandevelopment.celero_challeng_kt.api.ClientService
import com.stephenmorgandevelopment.celero_challeng_kt.api.MockClientService
import com.stephenmorgandevelopment.celero_challeng_kt.data.ClientDatabase
import com.stephenmorgandevelopment.celero_challeng_kt.data.ServiceModule
import com.stephenmorgandevelopment.celero_challeng_kt.doas.ClientDao
import com.stephenmorgandevelopment.celero_challeng_kt.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowNetworkCapabilities
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior

@RunWith(AndroidJUnit4::class)
class ClientRepoTest {
    private lateinit var clientRepo: ClientRepo

    @get:Rule var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val connectivityManager = setupConnectivityManager(context)

        val clientDatabase =
            Room.inMemoryDatabaseBuilder(context, ClientDatabase::class.java)
                .build()
        val clientDao = clientDatabase.clientDao()

        clientRepo = ClientRepo(
            connectivityManager,
            mockClientService(),
            clientDao
        )
    }

    @Test
    fun getClient() {

    }

    @Test
    fun getLiveClient() {
    }

    @Test
    fun getAll_doesNotReturnNotNull() = runBlockingTest{
        val liveClientList = clientRepo.getAll()

        val clientList = liveClientList.getOrAwaitValue()

        assertNotNull(clientList)
    }

    private fun setupConnectivityManager(context: Context) : ConnectivityManager {
        val conMan =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = ShadowNetworkCapabilities.newInstance()
        shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        shadowOf(conMan).setNetworkCapabilities(conMan.activeNetwork, networkCapabilities)

        return conMan
    }

    private fun mockClientService() : MockClientService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ServiceModule.BASE_URL)
            .build()

        val behaviour = NetworkBehavior.create()
        val mockRetrofit = MockRetrofit.Builder(retrofit)
            .networkBehavior(behaviour)
            .build()

        val delegate = mockRetrofit.create(ClientService::class.java)
        return MockClientService(delegate)
    }

}