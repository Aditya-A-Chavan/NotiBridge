package com.example.notibridge.authentication.repository

import com.example.notibridge.authentication.storage.PrefsManager
import com.example.notibridge.authentication.storage.SecureStore
import com.example.notibridge.network.NetworkManager
import com.example.notibridge.network.mdns.MdnsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ConnectionRepository(
    private val prefsManager: PrefsManager,
    private val secureStore: SecureStore,
    private val networkManager: NetworkManager,
    private val mdnsService: MdnsService
) {

    data class AuthResult(val success: Boolean, val errorMessage: String = "")


    suspend fun authenticate(phoneId: String, deviceId: String, hostname: String): AuthResult {
        return withContext(Dispatchers.IO) {
            val requestData = mapOf(
                "request" to "AUTHENTICATE",
                "phone_id" to phoneId,
                "device_id" to deviceId
            )

            val response = networkManager.sendRequest(hostname, requestData)

            return@withContext if (response["status"] == "SUCCESS") {
                AuthResult(success = true)
            } else {
                AuthResult(success = false, errorMessage = response["message"]?.toString() ?: "Authentication failed")
                
            }
        }
    }

    suspend fun attemptReconnection(): AuthResult {
        return withContext(Dispatchers.IO) {
            val phoneId = secureStore.getPhoneId() ?: return@withContext AuthResult(false, "No paired device")
            val deviceId = prefsManager.getDeviceId() ?: return@withContext AuthResult(false, "No paired device")
            val cachedHostname = prefsManager.getHostname()

            var hostname = cachedHostname ?: mdnsService.resolveDeviceHostname(deviceId)

            if (hostname == null) {
                return@withContext AuthResult(false, "Device not found on network")
            }

            val authResult = authenticate(phoneId, deviceId, hostname)
            if (authResult.success) {
                prefsManager.saveHostname(hostname) // Cache the resolved hostname for faster connections
            }
            return@withContext authResult
        }
    }
}
