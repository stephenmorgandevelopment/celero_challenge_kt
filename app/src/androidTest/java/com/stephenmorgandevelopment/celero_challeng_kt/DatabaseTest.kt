package com.stephenmorgandevelopment.celero_challeng_kt

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stephenmorgandevelopment.celero_challeng_kt.data.ClientDatabase
import com.stephenmorgandevelopment.celero_challeng_kt.doas.ClientDao
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import com.stephenmorgandevelopment.celero_challeng_kt.models.Location
import com.stephenmorgandevelopment.celero_challeng_kt.models.ProfilePicture
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsEqual.*
import org.junit.After
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var clientDao: ClientDao
    private lateinit var clientDatabase: ClientDatabase

    @Before
    fun init() {
        createDatabase()
        populateDatabaseWithMockData()
    }

    fun createDatabase() {
        val context: Context = ApplicationProvider.getApplicationContext()
        clientDatabase = Room.inMemoryDatabaseBuilder(
            context,
            ClientDatabase::class.java).build()
        clientDao = clientDatabase.clientDao()
    }

    fun populateDatabaseWithMockData() {
        runBlocking {
            clientDao.insertAll(mockData())
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        clientDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeAndReadEmptyClient() {
        val client: Client = Client.getEmpty()

        runBlocking {
            clientDao.insert(client)

            val phoneNumber = "(000)000-0000"

            assertThat(clientDao.load(0).phoneNumber, equalTo(phoneNumber))
        }

    }

    @Test
    @Throws(Exception::class)
    fun verifyClientListSize() {
        val simpleClients = clientDao.loadSimpleClients()

        assertEquals(simpleClients.size, 5)
    }

    @Test
    @Throws(Exception::class)
    fun checkDataFromList() {
        runBlocking {
            val client103 = clientDao.load(103)

            assertEquals(client103.name, "Mock 3")
            assertEquals(client103.visitOrder, 3)
        }
    }


    private fun mockData() : List<Client> {
        val mockedClients = ArrayList<Client>(5)
        for(i in 0 until 5 step 1) {
            mockedClients.add(
                Client(
                    (100L + i),
                    i.toLong(),
                    java.lang.String.format("Mock %d", i),
                    java.lang.String.format("(%d0%d)%d0%d-0000",i, i, i, i),
                    ProfilePicture.getEmpty(),
                    Location.getEmpty(),
                    java.lang.String.format("%d reasons", i),
                    listOf(String.format("10%d problems", i))
                )
            )
        }

        return mockedClients
    }


}