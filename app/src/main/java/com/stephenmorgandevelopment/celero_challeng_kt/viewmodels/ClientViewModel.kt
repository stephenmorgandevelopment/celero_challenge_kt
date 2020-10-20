package com.stephenmorgandevelopment.celero_challeng_kt.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import com.stephenmorgandevelopment.celero_challeng_kt.repos.ClientRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClientViewModel @ViewModelInject constructor(
    @Assisted val savedStateHandle: SavedStateHandle,
    val clientRepo: ClientRepo
) : ViewModel() {

    private var identifier: Long = savedStateHandle["identifier"] ?: -1
    private lateinit var _client: Client
    private lateinit var _liveClient: LiveData<Client>

    init {
        if (identifier != (-1).toLong()) {
            _liveClient = liveData {
                emitSource(clientRepo.getLiveClient(identifier))
            }
        }
    }

    fun setClient(id: Long) {
        identifier = id
        savedStateHandle.set("identifier", id)
        viewModelScope.launch(Dispatchers.IO) {
            _liveClient = clientRepo.getLiveClient(identifier)
        }
    }

    val client get() = _client
    val liveClient get() = _liveClient
}