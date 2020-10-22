package com.stephenmorgandevelopment.celero_challeng_kt.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.stephenmorgandevelopment.celero_challeng_kt.models.SimpleClient
import com.stephenmorgandevelopment.celero_challeng_kt.repos.ClientRepo
import dagger.hilt.android.qualifiers.ApplicationContext

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