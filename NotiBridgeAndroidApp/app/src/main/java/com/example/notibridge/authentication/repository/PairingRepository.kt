package com.example.notibridge.authentication.repository

import android.content.Context
import android.util.Log
import com.example.notibridge.authentication.storage.PrefsManager
import com.example.notibridge.authentication.storage.SecureStore
import com.example.notibridge.network.mdns.MdnsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.PrintWriter
import java.net.Socket

class PairingRepository(private val context: Context) {

    private val prefsManager = PrefsManager(context)
    private val secureStore = SecureStore(context)
    private val mdnsService = MdnsService(context)

    suspend fun initiatePairing(pairingKey: String, deviceId: String, onPairingComplete: (Boolean) -> Unit) {
        withContext(Dispatchers.IO) {
            mdnsService.discoverDesktop(deviceId) { hostname, ip ->
                Log.d("PairingRepository", "Attempting to pair with $hostname at $ip")

                try {
                    // Save pairing details securely
                    secureStore.saveSecureData("pairing_key", pairingKey)
                    secureStore.saveSecureData("phone_id", generatePhoneId())

                    // Save pairing data in Preferences
                    prefsManager.savePairingData(deviceId, hostname)

                    // Send pairing request
                    val success = sendPairingRequest(ip, 5000, pairingKey)

                    withContext(Dispatchers.Main) {
                        onPairingComplete(success)
                    }
                } catch (e: Exception) {
                    Log.e("PairingRepository", "Pairing failed: ${e.message}")
                    withContext(Dispatchers.Main) {
                        onPairingComplete(false)
                    }
                }
            }
        }
    }

    private fun sendPairingRequest(ip: String, port: Int, pairingKey: String): Boolean {
        return try {
            val socket = Socket(ip, port)
            val writer = PrintWriter(socket.getOutputStream(), true)
            writer.println("PAIR_REQUEST:$pairingKey")
            socket.close()
            true
        } catch (e: Exception) {
            Log.e("PairingRepository", "Pairing request failed: ${e.message}")
            false
        }
    }

    suspend fun isPaired(): Boolean = withContext(Dispatchers.IO) {
        prefsManager.isPaired()
    }

    suspend fun unpairDevice() {
        withContext(Dispatchers.IO) {
            secureStore.clearSecureData("pairing_key")
            secureStore.clearSecureData("phone_id")
            prefsManager.clearPairingData()
        }
    }

    fun startConnection(){
        println("Pending to write Connection Logic")
    }

    private fun generatePhoneId(): String {
        return java.util.UUID.randomUUID().toString()
    }
}
