package com.example.notibridge.notifications

import android.app.Service
import android.content.Intent
import android.os.IBinder
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

class NotificationService : Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var notificationListener: NotificationListener
    private lateinit var connectionRepository: ConnectionRepository

    override fun onCreate() {
        super.onCreate()
        
        val prefsManager = PrefsManager(this)
        val secureStore = SecureStore(this)
        val networkManager = NetworkManager()
        val mdnsService = MdnsService(this)
        
        connectionRepository = ConnectionRepository(prefsManager, secureStore, networkManager, mdnsService)
        notificationListener = NotificationListener { notification ->
            serviceScope.launch {
                try {
                    val notificationData = mapOf(
                        "type" to "NOTIFICATION",
                        "package_name" to notification.packageName,
                        "title" to notification.title,
                        "text" to notification.text,
                        "timestamp" to notification.timestamp
                    )
                    
                    connectionRepository.sendNotification(notificationData)
                } catch (e: Exception) {
                    Log.e("NotificationService", "Error sending notification: ${e.message}")
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationListener.startListening()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationListener.stopListening()
        connectionRepository.disconnect()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
