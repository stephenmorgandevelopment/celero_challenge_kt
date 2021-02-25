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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowNetworkCapabilities
import retrofit2.Retrofit
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit

/** ClientRepoTest will not run, because Room database will not work unless ran in
 *  an instrumented test.  This was moved here because roboelectric shadow objects
 *  will not work inside an instrumented test.
 */

class ClientRepoTest

//@RunWith(AndroidJUnit4::class)
//class ClientRepoTest {
//    private lateinit var clientRepo: ClientRepo
//    private lateinit var clientDatabase: ClientDatabase
//
//    @get:Rule var instantExecutorRule = InstantTaskExecutorRule()
//
//    @Before
//    fun setup() {
//        val context = ApplicationProvider.getApplicationContext<Context>()
//        val connectivityManager = setupConnectivityManager(context)
//
//        clientDatabase =
//            Room.inMemoryDatabaseBuilder(context, ClientDatabase::class.java)
//                .build()
//        val clientDao: ClientDao = clientDatabase.clientDao()
//
//        clientRepo = ClientRepo(
//            connectivityManager,
//            mockClientService(),
//            clientDao
//        )
//    }
//
//    @After
//    fun teardown() {
//        clientDatabase.close()
//    }
//
//    @Test
//    fun getClient() {
//
//    }
//
//    @Test
//    fun getLiveClient() {
//    }
//
//    @Test
//    fun getAll_doesNotReturnNotNull() = runBlocking {
//        val liveClientList = clientRepo.getAll()
//
//        val clientList = liveClientList.getOrAwaitValue()
//
//        assertNotNull(clientList)
//    }
//
//    private fun setupConnectivityManager(context: Context) : ConnectivityManager {
//        val conMan =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//        val networkCapabilities = ShadowNetworkCapabilities.newInstance()
//        shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
//        shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
//        shadowOf(conMan).setNetworkCapabilities(conMan.activeNetwork, networkCapabilities)
//
//        return conMan
//    }
//
//    private fun mockClientService() : MockClientService {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(ServiceModule.BASE_URL)
//            .build()
//
//        val behaviour = NetworkBehavior.create()
//        behaviour.setDelay(350, TimeUnit.MILLISECONDS)
//        behaviour.setFailurePercent(0)
//        val mockRetrofit = MockRetrofit.Builder(retrofit)
//            .networkBehavior(behaviour)
//            .build()
//
//        val delegate = mockRetrofit.create(ClientService::class.java)
//        return MockClientService(delegate)
//    }
//
//}