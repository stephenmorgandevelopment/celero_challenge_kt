package com.stephenmorgandevelopment.celero_challeng_kt.api

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import retrofit2.Call
import retrofit2.mock.BehaviorDelegate
import java.nio.charset.Charset

class MockAndroidClientService(
    private val delegate: BehaviorDelegate<ClientService>
): ClientService {
    private var response: List<Client>

    init {
        val fileStream = ApplicationProvider.getApplicationContext<Context>()
            .assets.open("testJson.json")

        val jsonString = fileStream.readBytes().toString(Charset.defaultCharset())

        fileStream.close()

        val listType = TypeToken
            .getParameterized(List::class.java, Client::class.java).type

        response = Gson().fromJson(jsonString, listType)
    }

    override fun getAllClients(list: String) : Call<List<Client>> =
        delegate.returningResponse(response).getAllClients(list)

    override fun getTestClients(list: String): Call<List<Client>> {
        TODO("Not yet implemented")
    }
//    fun getAllClients(list: String) : Call<List<Client>> {
//        val fileStream = ApplicationProvider.getApplicationContext<Context>()
//            .resources.openRawResource(R.raw.testJson)
//
//        val jsonString = fileStream.readBytes().toString(Charset.defaultCharset())
//        fileStream.close()
//
//        val listType = TypeToken
//            .getParameterized(List::class.java, Client::class.java).type

//        return Call<List<Client>>()
//        val clientList: List<Client> = Gson().fromJson(jsonString, listType)


//        return Calls.response(clientList)
    //        return clientList
//    }
}