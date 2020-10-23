package com.stephenmorgandevelopment.celero_challeng_kt.viewmodels

import android.os.Bundle
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import com.stephenmorgandevelopment.celero_challeng_kt.repos.ClientRepo

class ClientViewModel @ViewModelInject constructor(
    @Assisted val savedStateHandle: SavedStateHandle,
    private val clientRepo: ClientRepo
) : ViewModel() {

    private var identifier: Long = savedStateHandle["identifier"]
        ?: throw IllegalArgumentException("No identifier.")

    private lateinit var _liveClient: LiveData<Client>

    val liveClient get() = _liveClient

    init {
        if (identifier != (-1).toLong()) {
            _liveClient = liveData {
                emitSource(clientRepo.getLiveClient(identifier))
            }
        }
    }
}

class ClientViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val clientRepo: ClientRepo,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    // My lateinit attempt:
//    @Inject lateinit var clientRepo: ClientRepo

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return ClientViewModel(handle, clientRepo) as T
    }
}