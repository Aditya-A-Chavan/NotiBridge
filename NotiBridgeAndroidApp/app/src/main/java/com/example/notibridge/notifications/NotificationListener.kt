package com.example.notibridge.notifications

import android.app.Notification
import android.content.Context
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListener : NotificationListenerService() {
    private var onNotificationReceived: ((NotificationData) -> Unit)? = null

    override fun onCreate() {
        super.onCreate()
        Log.d("NotificationListener", "Service created")
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

        // Create notification data object
        val notificationData = NotificationData(
            packageName = packageName,
            title = title,
            text = text,
            timestamp = sbn.postTime
        )

        // Notify listener
        onNotificationReceived?.invoke(notificationData)
    }

    fun startListening() {
        Log.d("NotificationListener", "Starting notification listener")
    }

    fun stopListening() {
        Log.d("NotificationListener", "Stopping notification listener")
    }

    fun setOnNotificationReceivedListener(listener: (NotificationData) -> Unit) {
        onNotificationReceived = listener
    }

    data class NotificationData(
        val packageName: String,
        val title: String,
        val text: String,
        val timestamp: Long
    )
}
