package com.stephenmorgandevelopment.celero_challeng_kt

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stephenmorgandevelopment.celero_challeng_kt.api.ClientService
import com.stephenmorgandevelopment.celero_challeng_kt.data.ClientDatabase
import com.stephenmorgandevelopment.celero_challeng_kt.data.DataModule
import com.stephenmorgandevelopment.celero_challeng_kt.data.ServiceModule
import com.stephenmorgandevelopment.celero_challeng_kt.doas.ClientDao
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import com.stephenmorgandevelopment.celero_challeng_kt.models.Location
import com.stephenmorgandevelopment.celero_challeng_kt.models.ProfilePicture
import com.stephenmorgandevelopment.celero_challeng_kt.repos.ClientRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
//@ExperimentalCoroutinesApi
//@Config(application = HiltTestApplication::class)
class RepoTest {
    private val application: Context = ApplicationProvider.getApplicationContext<Context>()
    private lateinit var clientRepo: ClientRepo
    private lateinit var clientDatabase: ClientDatabase
    private lateinit var clientDao: ClientDao

//    @get:Rule
//    val hilt = HiltAndroidRule(this)

    @Before
    fun init() {
        initRepo()
        mockData()

//        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        clientDatabase.close()
//        Dispatchers.resetMain()
//        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getClient_returnsClientData() {
        val client = clientRepo.getClient(101L)

        assertEquals(client.name, "Mock 1")   //.equals("Mock 1")
        assertEquals(client.serviceReason, "1 reasons")
    }


    // Currently FAILS!!!
    @Test
    fun getAllReturnsData() {
        val clients = runBlocking {
            clientRepo.getAll()
        }

        assert(clients.value!!.size == 5)
    }


    fun initRepo() {
        //TODO Mock this service.  It is not good practice to test the network
        // calls here.
        val service = Retrofit.Builder()
            .baseUrl("https://hulet.tech")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ClientService::class.java)

        val conMan = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        clientDatabase =
            Room.inMemoryDatabaseBuilder(application, ClientDatabase::class.java)
                .build()

        clientDao = clientDatabase.clientDao()

//              DataModule.provideClientDao(DataModule.provideClientDatabase(application))

        clientRepo = ClientRepo(conMan, service, clientDao)
    }

    fun mockData() {
        val mockedClients = ArrayList<Client>(5)
        for (i in 0 until 5 step 1) {
            mockedClients.add(
                Client(
                    (100L + i),
                    i.toLong(),
                    java.lang.String.format("Mock %d", i),
                    java.lang.String.format("(%d0%d)%d0%d-0000", i, i, i, i),
                    ProfilePicture.getEmpty(),
                    Location.getEmpty(),
                    java.lang.String.format("%d reasons", i),
                    listOf(String.format("10%d problems", i))
                )
            )
        }

        runBlocking {
            clientDao.insertAll(mockedClients)
        }

    }

//        val mockClientOne = Client(
//            101,
//            1,
//            "Mock One",
//            "(101)101-0000",
//            ProfilePicture.getEmpty(),
//            Location.getEmpty(),
//            "101 reasons",
//            listOf("10%d problems")
}