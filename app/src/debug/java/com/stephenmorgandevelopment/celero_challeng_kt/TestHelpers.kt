package com.stephenmorgandevelopment.celero_challeng_kt

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import java.nio.charset.Charset

object TestHelpers {
    fun generateTestData() : List<Client> {
        val fileStream = ApplicationProvider.getApplicationContext<Context>()
            .assets.open("testJson.json")

        val jsonString = fileStream.readBytes().toString(Charset.defaultCharset())

        fileStream.close()

        val listType = TypeToken
            .getParameterized(List::class.java, Client::class.java).type

        return Gson().fromJson(jsonString, listType)
    }

}