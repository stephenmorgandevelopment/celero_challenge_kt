package com.stephenmorgandevelopment.celero_challeng_kt.repos

import androidx.lifecycle.LiveData
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import com.stephenmorgandevelopment.celero_challeng_kt.models.SimpleClient

interface DefaultClientRepo {

    fun getClient(identifier: Long) : Client

    fun getLiveClient(identifier: Long): LiveData<Client>

    suspend fun getAll(
        forceUpdate: Boolean = false,
        list: String? = null
    ): LiveData<List<SimpleClient>>
}