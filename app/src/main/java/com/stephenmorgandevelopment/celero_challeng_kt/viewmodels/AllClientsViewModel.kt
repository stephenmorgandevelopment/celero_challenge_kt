package com.stephenmorgandevelopment.celero_challeng_kt.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.stephenmorgandevelopment.celero_challeng_kt.models.SimpleClient
import com.stephenmorgandevelopment.celero_challeng_kt.repos.ClientRepo
import kotlinx.coroutines.launch

class AllClientsViewModel @ViewModelInject constructor(
    private val clientRepo: ClientRepo
) : ViewModel() {
    private var _allClients: LiveData<List<SimpleClient>>
    val clients : LiveData<List<SimpleClient>> get() = _allClients

    init {
        _allClients = liveData {
            emitSource(clientRepo.getAll())
        }
    }

    suspend fun refreshList() {
        _allClients = liveData {
            emitSource(clientRepo.getAll(true))
        }
    }

    // Test written to make sure list updated properly.
    // Writing unit test in the test folders will most likely be my next area of focused learning.
    suspend fun refreshTestList() {
        _allClients = liveData {
            emitSource(clientRepo.getTestList())
        }
    }
}