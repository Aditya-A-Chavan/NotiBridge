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
    private lateinit var connectionRepository: ConnectionRepository

    override fun onCreate() {
        super.onCreate()
        
        val prefsManager = PrefsManager(this)
        val secureStore = SecureStore(this)
        val networkManager = NetworkManager()
        val mdnsService = MdnsService(this)
        
        connectionRepository = ConnectionRepository(prefsManager, secureStore, networkManager, mdnsService)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // The NotificationListener service will be started by the system
        // when the user grants notification access permission
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        connectionRepository.disconnect()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        fun startService(context: android.content.Context) {
            val intent = Intent(context, NotificationService::class.java)
            context.startService(intent)
        }
    }
}
