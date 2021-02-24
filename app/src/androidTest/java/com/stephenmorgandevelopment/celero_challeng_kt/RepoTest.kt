package com.stephenmorgandevelopment.celero_challeng_kt

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.InvalidationTracker
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
import com.stephenmorgandevelopment.celero_challeng_kt.models.SimpleClient
import com.stephenmorgandevelopment.celero_challeng_kt.repos.ClientRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RepoTest

//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//@Config(application = HiltTestApplication::class)
//class RepoTest {
//    private val application: Context = ApplicationProvider.getApplicationContext<Context>()
//    private lateinit var clientRepo: ClientRepo
//    private lateinit var clientDatabase: ClientDatabase
//    private lateinit var clientDao: ClientDao
//
////    @Mock private lateinit var service: ClientService
//
////    @Module
////    @InstallIn(SingletonComponent::class)
////    object ServiceModule {
////        @Provides
////        fun provideClientService() =
////    }
//
////    @get:Rule val hilt = HiltAndroidRule(this)
//
//    @Before
//    fun init() {
//        initRepo()
//        insertMockData()
//    }
//
//    @After
//    fun tearDown() {
//        clientDatabase.close()
//    }
//
//    @Test
//    fun getClient_returnsClientData() {
//        val client = clientRepo.getClient(101L)
//
//        assertEquals(client.name, "Mock 1")   //.equals("Mock 1")
//        assertEquals(client.serviceReason, "1 reasons")
//    }
//
//    @Test
//    fun verifyDatabaseEntriesMatchMockedDataSize() {
//        var simpleClients: LiveData<List<SimpleClient>>
//
//        runBlocking(Dispatchers.Main) {
//            simpleClients = clientRepo.getAll()
//
//            simpleClients.observeForever(observer)
//
//            Thread.sleep(3000)
//
//            simpleClients.removeObserver(observer)
//        }
//    }
//
//    val observer = Observer<List<SimpleClient>>() {
//        assertEquals(it.size, 5)
//    }
//
//
//    private fun initRepo() {
//        val conMan =
//            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//        clientDatabase =
//            Room.inMemoryDatabaseBuilder(application, ClientDatabase::class.java)
//                .build()
//
//        clientDao = clientDatabase.clientDao()
//
////              DataModule.provideClientDao(DataModule.provideClientDatabase(application))
//
//        clientRepo = ClientRepo(conMan, null, clientDao)
//    }
//
//    private fun insertMockData() {
//        val mockedClients = TestingUtils.generateMockData()
//
//        runBlocking {
//            clientDao.insertAll(mockedClients)
//        }
//    }
//}