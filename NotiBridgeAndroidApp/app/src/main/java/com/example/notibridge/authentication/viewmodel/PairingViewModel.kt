package com.example.notibridge.authentication.viewmodel

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.notibridge.authentication.repository.PairingRepository
import com.example.notibridge.authentication.ui.PairingSuccessScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PairingViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PairingRepository(application)

    private val _isPaired = MutableStateFlow(true)
    val isPaired = _isPaired.asStateFlow()
    val isConnected = MutableStateFlow(false)

    init {
        checkPairingStatus()
    }

    private fun checkPairingStatus() {
        viewModelScope.launch {
            _isPaired.value = repository.isPaired()
        }
    }

    fun initiatePairing(pairingKey: String, deviceId: String, onPairingComplete: String) {
        viewModelScope.launch {
            repository.initiatePairing(pairingKey, deviceId) { success ->
                _isPaired.value = success
                onPairingComplete(success)
            }
        }
    }

    fun unpairDevice() {
        viewModelScope.launch {
            repository.unpairDevice()
            _isPaired.value = false
        }
    }

    @Composable
    fun requestHandling(conncted: Boolean, paired: Boolean){
        if(conncted && paired){
            PairingSuccessScreen(pairingViewModel)
        }
        else if(paired && !conncted){
            repository.startConnection()
        }
        else if(!paired && !conncted){
            repository.initiatePairing()
        }
        else{
            println("how can it be NOT paired but CONNECTED????")
        }
    }
}
