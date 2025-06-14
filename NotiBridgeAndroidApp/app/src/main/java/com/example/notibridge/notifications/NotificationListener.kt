package com.example.notibridge.notifications

import android.app.Notification
import android.content.Context
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.notibridge.authentication.repository.ConnectionRepository
import com.example.notibridge.authentication.storage.PrefsManager
import com.example.notibridge.authentication.storage.SecureStore
import com.example.notibridge.network.NetworkManager
import com.example.notibridge.network.mdns.MdnsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NotificationListener : NotificationListenerService() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var connectionRepository: ConnectionRepository

    override fun onCreate() {
        super.onCreate()
        Log.d("NotificationListener", "Service created")
        
        val prefsManager = PrefsManager(this)
        val secureStore = SecureStore(this)
        val networkManager = NetworkManager()
        val mdnsService = MdnsService(this)
        
        connectionRepository = ConnectionRepository(prefsManager, secureStore, networkManager, mdnsService)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val notification = sbn.notification
        val extras = notification.extras

        // Skip if no extras
        if (extras == null) return

        // Get notification data
        val packageName = sbn.packageName
        val title = extras.getString(Notification.EXTRA_TITLE) ?: ""
        val text = extras.getString(Notification.EXTRA_TEXT) ?: ""

        // Skip if text is empty or contains ignored content
        if (text.isBlank() || text == "No Content") return

        // Skip if contains ignored keywords
        val ignoredKeywords = listOf("Syncing new emails", "Background task running", "updating")
        if (ignoredKeywords.any { text.contains(it, ignoreCase = true) }) return

        // Skip if from ignored packages
        val ignoredPackages = listOf("com.google.android.gm", "com.android.systemui")
        if (ignoredPackages.contains(packageName)) return

        // Send notification through socket connection
        serviceScope.launch {
            try {
                val notificationData = mapOf(
                    "type" to "NOTIFICATION",
                    "package_name" to packageName,
                    "title" to title,
                    "text" to text,
                    "timestamp" to sbn.postTime
                )
                
                connectionRepository.sendNotification(notificationData)
            } catch (e: Exception) {
                Log.e("NotificationListener", "Error sending notification: ${e.message}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connectionRepository.disconnect()
    }
}
