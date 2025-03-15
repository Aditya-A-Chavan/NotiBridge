package com.example.notibridge.authentication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notibridge.authentication.repository.PairingRepository
import com.example.notibridge.authentication.repository.ConnectionRepository
import com.example.notibridge.authentication.storage.PrefsManager
import com.example.notibridge.authentication.storage.SecureStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider



class PairingViewModel(
    private val pairingRepository: PairingRepository,
    private val connectionRepository: ConnectionRepository,
    private val prefsManager: PrefsManager,
    private val secureStore: SecureStore
    ) : ViewModel(){

        enum class PairingState{
            UNPAIRED, PAIRED_DISCONNECTED, PAIRED_CONNECTED, ERROR
        }

        //Defaults to UNPAIRED
        //A mutable StateFlow that provides a setter for value.
        // An instance of MutableStateFlow with the given initial value can be
        // created using MutableStateFlow(value) constructor function.
        private val _pairingState = MutableStateFlow<PairingState>(PairingState.UNPAIRED)
        val pairingState: StateFlow<PairingState> = _pairingState


        private val _errorMessage = MutableStateFlow<String?>(null)
        val errorMessage: StateFlow<String?> = _errorMessage

        init {
            viewModelScope.launch {
                pairingState.collect { state ->
                    Log.d("PairingViewModel", "Pairing State Changed: $state")
                }
            }
        }


    //Fetches deviceId and phoneId from storage and checks state
        private fun checkPairingState(){
            viewModelScope.launch {
                val deviceId = prefsManager.getDeviceId()
                val phoneId = secureStore.getPhoneId()

                if(deviceId != null && phoneId != null){
                    _pairingState.value = PairingState.PAIRED_DISCONNECTED
                    attemptReconnection()
                }else{
                 _pairingState.value = PairingState.UNPAIRED
                }
            }
        }

        fun pairDevice(pairingKey: String, deviceId: String) {
            viewModelScope.launch{
                val phoneId = pairingRepository.generatePhoneId()
                val result = pairingRepository.pairWithDevice(pairingKey, deviceId, phoneId)

                if (result) {
                    secureStore.savePhoneId(phoneId)
//                    secureStore.savePairingkey(pairingKey)
                    prefsManager.saveDeviceId(deviceId)
//                    prefsManager.saveHostname(hostname)
                    _pairingState.value = PairingState.PAIRED_CONNECTED
                }else{
                    _errorMessage.value = "result.errorMessage"
                }
            }
        }

        fun attemptReconnection(){
            viewModelScope.launch {
                val hostname = prefsManager.getHostname()
                val deviceId = prefsManager.getDeviceId()
                val phoneId = secureStore.getPhoneId()

                if(hostname == null || deviceId == null || phoneId == null){
                    Log.e("Pairing View Model.attemptReconnection", "Unable to fetch hostname, deviceid, phoneid")
                    return@launch
                }

                val result = connectionRepository.authenticate(phoneId!!, deviceId!!, hostname!!)

                if(result.success){
                    _pairingState.value = PairingState.PAIRED_CONNECTED
                }else{
                    _pairingState.value = PairingState.PAIRED_DISCONNECTED
                }
            }
        }

        fun unpairDevice(){
            viewModelScope.launch {
                val phoneId = secureStore.getPhoneId()
                val deviceId = prefsManager.getDeviceId()
                Log.d("PairingViewModel", "Unpairing: phoneId=$phoneId, deviceId=$deviceId")

                if(deviceId == null || phoneId == null){
                    Log.e("PairingViewModel", "Cannot unpair, phoneId is null!")
                    return@launch
                }

                val result = pairingRepository.unpairDevice(phoneId)

                if(result.success){
                    secureStore.clearData()
                    prefsManager.clearData()
                    _pairingState.value = PairingState.UNPAIRED
                }else{
                    _errorMessage.value = result.errorMessage
                }
            }
        }
    }

class PairingViewModelFactory(
    private val pairingRepository: PairingRepository,
    private val connectionRepository: ConnectionRepository,
    private val prefsManager: PrefsManager,
    private val secureStore: SecureStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PairingViewModel::class.java)) {
            return PairingViewModel(pairingRepository, connectionRepository, prefsManager, secureStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
