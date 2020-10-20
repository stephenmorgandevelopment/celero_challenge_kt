package com.stephenmorgandevelopment.celero_challeng_kt.api

import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Singleton

@Singleton
interface ClientService {
    @GET("/{list}")
    fun getAllClients(@Path("list") list: String): Call<List<Client>>
}