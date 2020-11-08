package com.stephenmorgandevelopment.celero_challeng_kt.api

import android.util.Log
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import retrofit2.Response

class ClientApiResponse(private val response: Response<List<Client>>) {


    suspend fun processResponse() : List<Client> {
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            val error = response.errorBody()
            Log.d("ClientRepo", error.toString())
            throw Throwable(response.message())
        }
    }

}