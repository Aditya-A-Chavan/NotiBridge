package com.example.notibridge.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.notibridge.notifications.NotificationService

class ForegroundService : Service() {

    private lateinit var notificationService: NotificationService

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        notificationService = NotificationService(applicationContext) // Initialize NotificationService
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())

        Log.d(TAG, "ForegroundService started")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "ForegroundService running in background")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "ForegroundService stopped")
    }

    override fun onBind(intent: Intent?): IBinder? = null

     // Creates the notification channel for the foreground service.

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "NotiBridge Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Keeps NotiBridge running in the background"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    //Creates the persistent notification for the foreground service.
    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, ForegroundService::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )

        return Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("NotiBridge Running")
            .setContentText("Notification service is active")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    companion object {
        private const val TAG = "ForegroundService"
        private const val CHANNEL_ID = "notibridge_service_channel"
        private const val NOTIFICATION_ID = 1

        /**
         * Starts the foreground service.
         */
        fun startService(context: Context) {
            val intent = Intent(context, ForegroundService::class.java)
            context.startForegroundService(intent)
            Log.d("ForeGroundServiceDebug", "Started The bg service")
        }

      //stops Service
        fun stopService(context: Context) {
            val intent = Intent(context, ForegroundService::class.java)
            context.stopService(intent)
        }
    }
}
