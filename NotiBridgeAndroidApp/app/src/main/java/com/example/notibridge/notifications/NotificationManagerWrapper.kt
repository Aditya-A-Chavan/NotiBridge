package com.example.notibridge.notifications

import android.content.Context
import android.util.Log
import com.example.notibridge.network.NetworkManager
import com.example.notibridge.authentication.storage.PrefsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationManagerWrapper(private val context: Context) {

    private val prefsManager = PrefsManager(context)
    private val networkManager = NetworkManager() // Instance of NetworkManager
    suspend fun sendNotification(packageName: String, title: String, text: String) {
        Log.d("NotificationManager", "Preparing to send notification: [$packageName] $title: $text")

        val hostIp = withContext(Dispatchers.IO) { prefsManager.getHostIp() }

        if (hostIp.isNullOrEmpty()) {
            Log.e("NotificationManager", "Failed to send notification: No host IP found")
            return
        }

        // Prepare the request data
        val requestData = mapOf(
            "request" to "NOTIFICATION",
            "package_name" to packageName,
            "title" to title,
            "text" to text,
            "timestamp" to System.currentTimeMillis().toString()
        )

        val response = networkManager.sendRequest(hostIp, requestData)

        if (response["status"] == "SUCCESS") {
            Log.d("NotificationManager", "Notification sent successfully")
        } else {
            Log.e("NotificationManager", "Failed to send notification: ${response["message"]}")
        }
    }
}
