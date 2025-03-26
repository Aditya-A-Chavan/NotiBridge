package com.example.notibridge.notifications

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListener : NotificationListenerService() {

    private lateinit var notificationService: NotificationService

    override fun onCreate() {
        super.onCreate()
        notificationService = NotificationService(applicationContext) // Initialize NotificationService
        Log.d(TAG, "Notification Listener Service Created")
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "Notification Listener connected")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn == null) return

        val packageName = sbn.packageName
        val extras = sbn.notification.extras

        val title = extras.getString(Notification.EXTRA_TITLE, "No Title")
        val text = extras.getCharSequence(Notification.EXTRA_TEXT, "No Content")?.toString()

        if (text.isNullOrBlank()) return

        Log.d(TAG, "Notification Received: [$packageName], $title: $text")

        notificationService.processNotification(packageName, title, text)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
//        Log.d(TAG, "Notification removed: ${sbn?.packageName}")
    }

    companion object {
        private const val TAG = "NotificationListener"
    }
}
