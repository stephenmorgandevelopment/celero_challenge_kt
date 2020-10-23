package com.stephenmorgandevelopment.celero_challeng_kt.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.stephenmorgandevelopment.celero_challeng_kt.models.SimpleClient
import com.stephenmorgandevelopment.celero_challeng_kt.repos.ClientRepo

class AllClientsViewModel @ViewModelInject constructor(
    private val _clientRepo: ClientRepo
) : ViewModel() {
    // Left this list val here for the potential of setting it at runtime.
    // i.e.- Multiple handymen, each with their own token / lists.
    private val list = "celerocustomers.json"

    // Since I'm passing the repo around. (A work in progress)
    // I thought I would protect the originally injected object Hilt/Dagger provides.
    val clientRepo get() = _clientRepo

    // I'm trying to figure out an implementation that maintains the same
    // _allClients LiveData object through the scope of the ViewModel,
    // but I haven't figured it out yet.  LiveData / Room / Coroutines etc is so new to me still.
    private var _allClients: LiveData<List<SimpleClient>>
    val clients get() = _allClients

    init {
        _allClients = liveData {
            emitSource(_clientRepo.getAll(list))
        }

    }

    suspend fun refreshList() {
        _allClients = liveData {
            emitSource(_clientRepo.getAll(list))
        }
    }

    // Test written to make sure list updated properly.
    // Writing unit test in the test folders will most likely be my next area of focused learning.
    suspend fun refreshTestList() {
        _allClients = liveData {
            emitSource(_clientRepo.getTestList("clients.json"))
        }
    }
}