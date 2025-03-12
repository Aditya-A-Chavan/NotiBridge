package com.example.notibridge.authentication.repository

import com.example.notibridge.authentication.storage.PrefsManager
import com.example.notibridge.authentication.storage.SecureStore
import com.example.notibridge.network.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID


class PairingRepository(
    private val secureStore: SecureStore,
    private val prefsManager: PrefsManager,
    private val networkManager: NetworkManager // TODO: implement network manager
){

    //Return Class for whenever Pairing Repository is called
    data class PairingResult(val success: Boolean, val errorMessage: String = "")

    fun generatePhoneId(): String {
        val phoneId = UUID.randomUUID().toString()
        println(phoneId)
        return phoneId
    }

//    suspend fun pairWithDevice(pairingKey: String, deviceId: String, phoneId: String, hostname: String){
//        return withContext(Dispatchers.IO){
//            val requestData = mapOf(
//                "request" to "PAIR",
//                "device_id" to deviceId,
//                "phone_id" to phoneId,
//                "pairing_key" to pairingKey
//            )
//
//            val response = networkManager.sendRequest(hostname, requestData)
//
//            if(response["status"] == "SUCCESS"){
//                secureStore.savePhoneId(phoneId)
//                secureStore.savePairingKey(pairingKey)
//                prefsManager.saveDeviceId(deviceId)
//                prefsManager.saveHostname(hostname)
//
//                PairingResult(success = true)
//            }else{
//                PairingResult(success = false, errorMessage = response["message"] ?: "pairing failed") //default to pairing failed
//            }
//        }
//    }

    suspend fun pairWithDevice(pairingKey: String, deviceId: String, phoneId: String, hostname: String): Boolean {
        val requestData = mapOf(
               "request" to "PAIR",
                "device_id" to deviceId,
                "phone_id" to phoneId,
                "pairing_key" to pairingKey
        )
        val response = networkManager.sendRequest(hostname, requestData)

        if(response["status"] == "SUCCESS") {
            secureStore.savePhoneId(phoneId)
            secureStore.savePairingKey(pairingKey)
            prefsManager.saveDeviceId(deviceId)
            prefsManager.saveHostname(hostname)

            return true
        }else{
            return false
        }
    }

    suspend fun unpairDevice(phoneId: String): PairingResult{
        return withContext(Dispatchers.IO){
            val hostname = prefsManager.getHostname() ?: return@withContext PairingResult(false, "No Paired Device to Unpair")

            val requestData = mapOf(
                "request" to "UNPAIR",
                "phone_id" to phoneId
            )

            val response = networkManager.sendRequest(hostname, requestData)

            return@withContext if(response["status"] == "SUCCESS"){
                secureStore.clearData()
                prefsManager.clearData()
                PairingResult(success = true)
            }else{
                PairingResult(success = false)
            }
        }
    }
}


