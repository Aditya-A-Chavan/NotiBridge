package com.example.notibridge.authentication.repository

import android.util.Log
import com.example.notibridge.authentication.storage.PrefsManager
import com.example.notibridge.authentication.storage.SecureStore
import com.example.notibridge.network.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import com.example.notibridge.network.mdns.MdnsService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.time.withTimeoutOrNull
import kotlinx.coroutines.withTimeoutOrNull

class PairingRepository(
    private val secureStore: SecureStore,
    private val prefsManager: PrefsManager,
    private val networkManager: NetworkManager,
    private val mdnsService: MdnsService
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

    suspend fun pairWithDevice(pairingKey: String, deviceId: String, phoneId: String): Boolean {
        Log.d("Pairing repository.pairWithDevice", "Attempting pair procedure with: $deviceId")

        val hostIp = withContext(Dispatchers.IO) {
            val result = CompletableDeferred<String?>()

            mdnsService.startMdnsDiscovery("_notibridge._tcp.") { ip, hostname ->
                if (hostname == deviceId) {
                    result.complete(ip)
                }
            }

            withTimeoutOrNull(3000) { result.await() }
        }

        if (hostIp == null) {
            Log.e("Pairing repository.pairWithDevice", "Failed to resolve device IP for deviceId: $deviceId")
            return false
        }

        Log.d("mDNS Output", "Resolved IP: $hostIp for deviceId: $deviceId")

        // Prepare the request data
        val requestData = mapOf(
            "request" to "PAIR",
            "device_id" to deviceId,
            "phone_id" to phoneId,
            "pairing_key" to pairingKey
        )

        val response = networkManager.sendRequest(hostIp, requestData)

        return if (response["status"] == "SUCCESS") {
            secureStore.savePhoneId(phoneId)
            secureStore.savePairingKey(pairingKey)
            prefsManager.saveDeviceId(deviceId)
            Log.d("Pairing repository.pairWithDevice", "Pairing successful for deviceId: $deviceId")
            true
        } else {
            Log.e("Pairing repository.pairWithDevice", "Pairing failed for deviceId: $deviceId")
            false
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


