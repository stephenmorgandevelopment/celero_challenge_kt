package com.stephenmorgandevelopment.celero_challeng_kt.repos

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stephenmorgandevelopment.celero_challeng_kt.TestHelpers
import com.stephenmorgandevelopment.celero_challeng_kt.TestingUtils
import com.stephenmorgandevelopment.celero_challeng_kt.api.ClientService
import com.stephenmorgandevelopment.celero_challeng_kt.api.MockAndroidClientService
import com.stephenmorgandevelopment.celero_challeng_kt.data.ClientDatabase
import com.stephenmorgandevelopment.celero_challeng_kt.data.ServiceModule
import com.stephenmorgandevelopment.celero_challeng_kt.doas.ClientDao
import com.stephenmorgandevelopment.celero_challeng_kt.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior
import java.util.concurrent.TimeUnit

/** ClientRepoAndroidTest works as expected, but requires that the emulator or device
 *  be connected to the internet.  This is because roboelectric shadow objects will
 *  not work in an instrumented environment, and the ClientRepo requires a
 *  ConnectivityManager to check for connection before making api calls.
 */


@RunWith(AndroidJUnit4::class)
class ClientRepoAndroidTest {
    private lateinit var clientRepo: ClientRepo
    private lateinit var clientDatabase: ClientDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        clientDatabase =
            Room.inMemoryDatabaseBuilder(context, ClientDatabase::class.java)
                .build()
        val clientDao: ClientDao = clientDatabase.clientDao()

        clientRepo = ClientRepo(
            connectivityManager,
            mockClientService(),
            clientDao
        )
    }

    @After
    fun teardown() {
        clientDatabase.close()
    }

    @Test
    fun getClient_returnsCorrectClientByIdentifier() = runBlocking {
        val testData = TestHelpers.generateTestData()
        val clientIdentifiers = listOf<Long>(234513L, 734563L, 364562L)

        // Load data into database
        val simpleClients = clientRepo.getAll()

        assert(clientRepo.getClient(clientIdentifiers[0]).name == testData[0].name)
        assert(clientRepo.getClient(clientIdentifiers[1]).name == testData[1].name)
        assert(clientRepo.getClient(clientIdentifiers[2]).name == testData[2].name)
    }


    @Test
    fun getLiveClient_returnsCorrectClientByIdentifierAsLiveData() = runBlocking {
        val testData = TestHelpers.generateTestData()
        val clientIdentifiers = listOf<Long>(234513L, 734563L, 364562L)

        // Load data into database
        val simpleClients = clientRepo.getAll()

        assert(clientRepo.getLiveClient(clientIdentifiers[0])
            .getOrAwaitValue().name == testData[0].name)

        assert(clientRepo.getLiveClient(clientIdentifiers[1])
                .getOrAwaitValue().name == testData[1].name)

        assert(clientRepo.getLiveClient(clientIdentifiers[2])
                .getOrAwaitValue().name == testData[2].name)
    }

    @Test
    fun getAll_doesNotReturnNotNull() = runBlocking {
        val liveClientList = clientRepo.getAll()

        val clientList = liveClientList.getOrAwaitValue()

        assertNotNull(clientList)
    }

    @Test
    fun getAll_clientListSizeMatchesTestFile() = runBlocking {
        val expectedSize = 3

        val liveClientList = clientRepo.getAll()

        val clientList = liveClientList.getOrAwaitValue()

        assert(clientList.size == expectedSize)
    }

    @Test
    fun getAll_firstClientDataMatchesExpectedData() = runBlocking {
        val expectedIdentifier = 234513L
        val expectedName = "Michael Hulet"
        val expectedServiceReason = "I have a leaky faucet"

        val liveClientList = clientRepo.getAll()

        val clientList = liveClientList.getOrAwaitValue()
        val firstClient = clientList[0]

        assert(firstClient.identifier == expectedIdentifier)
        assert(firstClient.name == expectedName)
        assert(firstClient.serviceReason == expectedServiceReason)
    }


    private fun mockClientService(): MockAndroidClientService {
        val retrofit = Retrofit.Builder()
            .baseUrl(ServiceModule.BASE_URL)
            .build()

        val behaviour = NetworkBehavior.create()
        behaviour.setDelay(350, TimeUnit.MILLISECONDS)
        behaviour.setFailurePercent(0)
        val mockRetrofit = MockRetrofit.Builder(retrofit)
            .networkBehavior(behaviour)
            .build()

        val delegate = mockRetrofit.create(ClientService::class.java)
        return MockAndroidClientService(delegate)
    }

}