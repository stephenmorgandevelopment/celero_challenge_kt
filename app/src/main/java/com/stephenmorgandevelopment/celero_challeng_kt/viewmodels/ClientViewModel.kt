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
    private val clientRepo: ClientRepo
) : ViewModel() {
    private val _liveClient: MutableLiveData<Client> = MutableLiveData()
    val liveClient: LiveData<Client> get() = _liveClient

    val identifier : Long = savedStateHandle[KEY_USER] ?: -1

    init {
        if(identifier != -1L) {
            viewModelScope.launch(Dispatchers.IO) {
                _liveClient.postValue(clientRepo.getClient(identifier))
            }
        } else {
            _liveClient.value = Client.getEmpty()
        }
    }

    fun setClient(id: Long) {
        savedStateHandle.set(KEY_USER, id)
        viewModelScope.launch(Dispatchers.IO) {
            _liveClient.postValue(clientRepo.getClient(id))
        }
    }

    companion object {
        private val KEY_USER = "identifier"
    }


}

