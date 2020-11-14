package com.stephenmorgandevelopment.celero_challeng_kt

//import android.content.Context
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.google.common.truth.Truth.*
//import com.stephenmorgandevelopment.celero_challeng_kt.data.DataModule
//import com.stephenmorgandevelopment.celero_challeng_kt.data.ServiceModule
//import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
//import com.stephenmorgandevelopment.celero_challeng_kt.models.Location
//import com.stephenmorgandevelopment.celero_challeng_kt.models.ProfilePicture
//import com.stephenmorgandevelopment.celero_challeng_kt.repos.ClientRepo
//import kotlinx.coroutines.*
//import kotlinx.coroutines.test.TestCoroutineDispatcher
//import kotlinx.coroutines.test.resetMain
//import kotlinx.coroutines.test.setMain
//import org.junit.After
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import java.lang.String.format
//import org.junit.runners.JUnit4


class RepoTestOld {

}

//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//@ExperimentalCoroutinesApi
//@Config(application = HiltTestApplication::class)
//class RepoTestOld {
//    private val application: Context = ApplicationProvider.getApplicationContext<Context>()
//    private lateinit var clientRepo: ClientRepo
//    val dispatcher = TestCoroutineDispatcher()
//
////    @get:Rule
////    val hilt = HiltAndroidRule(this)
//
//    @Before
//    fun init() {
//        initRepo()
//        mockData()
//
//        Dispatchers.setMain(dispatcher)
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//        dispatcher.cleanupTestCoroutines()
//    }
//
//    @Test
//    fun getClient_returnsClientData() {
//        val client = clientRepo.getClient(101L)
//
//        assertThat(client.name).isEqualTo("Mock 1")
//        assertThat(client.serviceReason).isEqualTo("1 reason")
//
//    }
//
//
//    @Test
//    fun getAllReturnsData() {
//        val clients = runBlocking {
//            clientRepo.getAll()
//        }
//
//        assertThat(clients.value?.size).isEqualTo(5)
//    }
//
//
//
//    fun initRepo() {
//        val service = ServiceModule.provideClientService()
//        val conMan = ServiceModule.provideConnectivityManager(application)
//        val clientDao =
//            DataModule.provideClientDao(DataModule.provideClientDatabase(application))
//
//        clientRepo = ClientRepo(conMan, service, clientDao)
//    }
//
//    fun mockData() {
//        val mockedClients = ArrayList<Client>(5)
//        for(i in 0 until 5 step 1) {
//            mockedClients.add(
//                Client(
//                    (100L + i),
//                    i.toLong(),
//                    format("Mock %d", i),
//                    format("(%d0%d)%d0%d-0000",i, i, i, i),
//                    ProfilePicture.getEmpty(),
//                    Location.getEmpty(),
//                    format("%i reasons", i),
//                    listOf(String.format("10%d problems", i))
//                )
//            )
//        }
//
//        val clientDao =
//            DataModule.provideClientDao(DataModule.provideClientDatabase(application))
//
//        runBlocking {
//            clientDao.insertAll(mockedClients)
//        }
//
//    }
//}

//        val mockClientOne = Client(
//            101,
//            1,
//            "Mock One",
//            "(101)101-0000",
//            ProfilePicture.getEmpty(),
//            Location.getEmpty(),
//            "101 reasons",
//            listOf("10%d problems")
//        )