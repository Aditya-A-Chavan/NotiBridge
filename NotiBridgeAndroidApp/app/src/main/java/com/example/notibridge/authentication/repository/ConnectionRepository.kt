package com.example.notibridge.authentication.repository

import android.util.Log
import com.example.notibridge.authentication.storage.PrefsManager
import com.example.notibridge.authentication.storage.SecureStore
import com.example.notibridge.network.NetworkManager
import com.example.notibridge.network.mdns.MdnsService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.time.withTimeoutOrNull
import kotlinx.coroutines.withContext

class ConnectionRepository(
    private val prefsManager: PrefsManager,
    private val secureStore: SecureStore,
    private val networkManager: NetworkManager,
    private val mdnsService: MdnsService
) {

//    data class AuthResult(val success: Boolean, val errorMessage: String = "")


    suspend fun authenticate(phoneId: String, deviceId: String, currhostIp: String?): Boolean {
        return withContext(Dispatchers.IO) {

            Log.d("ConnectionRepository.authenticate", "Attempting authentication with $deviceId")

            val hostIp = withContext(Dispatchers.IO){
                val result = CompletableDeferred<String?>()

                mdnsService.startMdnsDiscovery("_notibridge._tcp."){ip, hostname ->
                    if(hostname == deviceId){
                        result.complete(ip)
                    }
                }
                kotlinx.coroutines.withTimeoutOrNull(3000) { result.await() }
            }

            if(hostIp == null){
                Log.e("ConnectionRepository.authenticate", "hostIp returned as null from mdns Service")
                return@withContext false
            }



            Log.d("mDNS Output", "Resolved IP: $hostIp for deviceId: $deviceId")

            val requestData = mapOf(
                "request" to "AUTHENTICATE",
                "phone_id" to phoneId,
                "device_id" to deviceId
            )


            val response = networkManager.sendRequest(hostIp, requestData)

            return@withContext if (response["status"] == "SUCCESS") {

                //Updating new Ip
                if(hostIp != currhostIp){
                    prefsManager.saveHostIp(hostIp)
                    Log.d("ConnectionRepository.authenticate", "HostIp updated to $hostIp")
                }
                true

            } else {
                Log.e("ConnectionRepository.authenticate", "Pairing Unsuccessful")
                false

            }
        }
    }


//    suspend fun attemptReconnection(): AuthResult {
//        return withContext(Dispatchers.IO) {
//            val phoneId = secureStore.getPhoneId() ?: return@withContext AuthResult(false, "No paired device")
//            val deviceId = prefsManager.getDeviceId() ?: return@withContext AuthResult(false, "No paired device")
//            val cachedHostname = prefsManager.getHostname()
//
////            var hostname = cachedHostname ?: mdnsService.resolveDeviceHostname(deviceId)
//            var hostname = "DAMN"
//            if (hostname == null) {
//                return@withContext AuthResult(false, "Device not found on network")
//            }
//
//            val authResult = authenticate(phoneId, deviceId, hostname)
//            if (authResult.success) {
//                prefsManager.saveHostname(hostname) // Cache the resolved hostname for faster connections
//            }
//            return@withContext authResult
//        }
//    }
}
