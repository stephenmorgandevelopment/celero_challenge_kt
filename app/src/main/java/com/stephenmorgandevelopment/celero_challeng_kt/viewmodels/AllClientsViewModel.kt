package com.stephenmorgandevelopment.celero_challeng_kt.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.stephenmorgandevelopment.celero_challeng_kt.models.SimpleClient
import com.stephenmorgandevelopment.celero_challeng_kt.repos.ClientRepo

class AllClientsViewModel @ViewModelInject constructor(
    clientRepo: ClientRepo
) : ViewModel() {
    private val list = "celerocustomers.json"

    private val _allClients: LiveData<List<SimpleClient>>
    val clients get() = _allClients

    init {
        _allClients = liveData {
            emitSource(clientRepo.getAll(list))
        }
    }
}