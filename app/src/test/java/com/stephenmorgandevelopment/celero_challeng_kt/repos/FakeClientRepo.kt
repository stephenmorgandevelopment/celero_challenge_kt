package com.stephenmorgandevelopment.celero_challeng_kt.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import com.stephenmorgandevelopment.celero_challeng_kt.models.Location
import com.stephenmorgandevelopment.celero_challeng_kt.models.ProfilePicture
import com.stephenmorgandevelopment.celero_challeng_kt.models.SimpleClient

class FakeClientRepo : DefaultClientRepo {

    private var lastFetchSaved: Long = -1

    private lateinit var clients: List<Client>

    private lateinit var simpleClients: List<SimpleClient>

    override fun getClient(identifier: Long): Client {
        return clients.get(identifier.toInt() + timesRan)
    }

    override fun getLiveClient(identifier: Long): LiveData<Client> {
        val liveData = MutableLiveData<Client>()
        liveData.postValue(getClient(identifier))
        return liveData
    }

    override suspend fun getAll(
        forceUpdate: Boolean,
        list: String?
    ): LiveData<List<SimpleClient>> {
        if (needsRefreshed() || forceUpdate) {
            refreshList()
        }

        val liveData = MutableLiveData<List<SimpleClient>>()
        liveData.postValue(simpleClients)

        return liveData
    }

    private var timesRan = 0
    private suspend fun refreshList() {
        clients = generateMockData(timesRan)
        simpleClients = generateMockSimpleClients(timesRan++)
        lastFetchSaved = System.currentTimeMillis()
    }


    private fun needsRefreshed(
        timeout: Long = 300000
    ): Boolean {
        return ((System.currentTimeMillis() - lastFetchSaved) > timeout)
    }

    companion object {
        fun generateMockSimpleClients(offset: Int = 0): List<SimpleClient> {
            val mockedClients = ArrayList<SimpleClient>(5)
            for (i in offset until (offset + 5)) {
                mockedClients.add(
                    SimpleClient(
                        (100L + i),
                        java.lang.String.format("Mock %d", i),
                        java.lang.String.format("%d reasons", i)
                    )
                )
            }

            return mockedClients
        }


        fun generateMockData(offset: Int = 0): List<Client> {
            val mockedClients = ArrayList<Client>(5)
            for (i in offset..(offset + 5)) {
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

            return mockedClients
        }

    }
}