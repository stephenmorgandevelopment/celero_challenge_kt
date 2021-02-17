package com.stephenmorgandevelopment.celero_challeng_kt.viewmodels

import androidx.lifecycle.*
import com.stephenmorgandevelopment.celero_challeng_kt.models.Client
import com.stephenmorgandevelopment.celero_challeng_kt.repos.DefaultClientRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val clientRepo: DefaultClientRepo
) : ViewModel() {
    private val _liveClient: MutableLiveData<Client> = MutableLiveData()
    val liveClient: LiveData<Client> get() = _liveClient

    private var _identifier: Long = savedStateHandle[KEY_USER] ?: -1L
    var identifier: Long = _identifier

    init {
        if (_identifier != -1L) {
            viewModelScope.launch(Dispatchers.IO) {
                _liveClient.postValue(clientRepo.getClient(_identifier))
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
        _identifier = id
    }

    companion object {
        private val KEY_USER = "identifier"
    }
}

