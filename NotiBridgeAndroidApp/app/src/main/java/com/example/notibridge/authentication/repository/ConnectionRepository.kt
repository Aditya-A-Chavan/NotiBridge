package com.example.notibridge.authentication.repository

import android.util.Log
import com.example.notibridge.authentication.storage.PrefsManager
import com.example.notibridge.authentication.storage.SecureStore
import com.example.notibridge.network.NetworkManager
import com.example.notibridge.network.SocketConnectionManager
import com.example.notibridge.network.mdns.MdnsService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.time.withTimeoutOrNull
import kotlinx.coroutines.withContext

class ConnectionRepository(
    private val prefsManager: PrefsManager,
    private val secureStore: SecureStore,
    private val networkManager: NetworkManager,
    private val mdnsService: MdnsService
) {
    private val socketConnectionManager = SocketConnectionManager()

    suspend fun authenticate(phoneId: String, deviceId: String, currhostIp: String?, pairingKey: String?): Boolean {
        return withContext(Dispatchers.IO) {
            Log.d("ConnectionRepository.authenticate", "Attempting authentication with $deviceId")

            val hostIp = withContext(Dispatchers.IO) {
                val result = CompletableDeferred<String?>()

                mdnsService.startMdnsDiscovery("_notibridge._tcp.") { ip, hostname ->
                    if (hostname == deviceId) {
                        result.complete(ip)
                    }
                }
                kotlinx.coroutines.withTimeoutOrNull(3000) { result.await() }
            }

            if (hostIp == null) {
                Log.e("ConnectionRepository.authenticate", "hostIp returned as null from mdns Service")
                return@withContext false
            }

            Log.d("mDNS Output", "Resolved IP: $hostIp for deviceId: $deviceId")

            val requestData = mapOf(
                "request" to "AUTHENTICATE",
                "phone_id" to phoneId,
                "device_id" to deviceId,
                "pairing_key" to pairingKey,
            )

            val response = networkManager.sendRequest(hostIp, requestData)

            if (response["status"] == "SUCCESS") {
                // Update new IP if changed
                if (hostIp != currhostIp) {
                    prefsManager.saveHostIp(hostIp)
                    Log.d("ConnectionRepository.authenticate", "HostIp updated to $hostIp")
                }

                // Establish persistent socket connection
                val connected = socketConnectionManager.connect(hostIp)
                if (!connected) {
                    Log.e("ConnectionRepository.authenticate", "Failed to establish persistent connection")
                    return@withContext false
                }

                true
            } else {
                Log.e("ConnectionRepository.authenticate", "Authentication Unsuccessful")
                false
            }
        }
    }

    suspend fun sendNotification(notificationData: Map<String, Any?>): Boolean {
        return withContext(Dispatchers.IO) {
            val connectionState = socketConnectionManager.connectionState.first()
            
//            if (connectionState == SocketConnectionManager.ConnectionState.CONNECTED) {
//
//            } else {
//                // If not connected, try to reconnect
//                val hostIp = prefsManager.getHostIp()
//                val deviceId = prefsManager.getDeviceId()
//                val phoneId = secureStore.getPhoneId()
//                val pairingKey = secureStore.getPairingKey()
//
//                if (hostIp == null || deviceId == null || phoneId == null) {
//                    Log.e("ConnectionRepository.sendNotification", "Missing connection data")
//                    return@withContext false
//                }
//
//                // Attempt to reconnect
//                val reconnected = authenticate(phoneId, deviceId, hostIp, pairingKey)
//                if (reconnected) {
//                    socketConnectionManager.sendMessage(notificationData)
//                } else {
//                    false
//                }
//            }
            socketConnectionManager.sendMessage(notificationData)
        }
    }

    fun getConnectionState() = socketConnectionManager.connectionState

    fun disconnect() {
        socketConnectionManager.disconnect()
    }
}
