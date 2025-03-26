package com.example.notibridge.notifications

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationService(private val context: Context) {

    private val notificationManager = NotificationManagerWrapper(context)

    fun processNotification(packageName: String, title: String, text: String) {
//        Log.d(TAG, "Processing notification: [$packageName] $title: $text")

        //if text is empty or "No Content"
        if (text.isBlank() || text == "No Content"){
            return
        }

        //if contains ignored keywords
        val ignoredKeywords = listOf("Syncing new emails", "Background task running", "updating")
        if(ignoredKeywords.any { text.contains(it, ignoreCase = true) }) {
            return
        }

        //if contains ignored packages
        val ignoredPackages = listOf("com.google.android.gm", "com.android.systemui")
        if(ignoredPackages.contains(packageName)) {
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            notificationManager.sendNotification(packageName, title, text)
        }
    }

    companion object {
        private const val TAG = "NotificationService"
    }
}
