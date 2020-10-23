package com.stephenmorgandevelopment.celero_challeng_kt.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.stephenmorgandevelopment.celero_challeng_kt.models.SimpleClient
import com.stephenmorgandevelopment.celero_challeng_kt.repos.ClientRepo

class AllClientsViewModel @ViewModelInject constructor(
    val clientRepo: ClientRepo
) : ViewModel() {
    private val list = "celerocustomers.json"

    private var _allClients: LiveData<List<SimpleClient>>
    val clients get() = _allClients

    init {
        _allClients = liveData {
            emitSource(clientRepo.getAll(list))
        }

    }

    suspend fun refreshList() {
        _allClients = liveData {
            emitSource(clientRepo.getAll(list))
        }
    }

    // Test written to make sure list updated properly.
    suspend fun refreshTestList() {
        _allClients = liveData {
            emitSource(clientRepo.getTestList("clients.json"))
        }
    }
}