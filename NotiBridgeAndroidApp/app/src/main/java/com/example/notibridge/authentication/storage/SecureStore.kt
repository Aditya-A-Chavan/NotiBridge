package com.example.notibridge.authentication.storage

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import java.security.SecureRandom

class SecureStore(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("secure_store", Context.MODE_PRIVATE)

    companion object {
        private const val PHONE_ID_KEY = "phone_id"
        private const val PAIRING_KEY = "pairing_key"
    }

    fun savePhoneId(phoneId: String) {
        sharedPreferences.edit().putString(PHONE_ID_KEY, phoneId).apply()
    }

    fun getPhoneId(): String? {
        return sharedPreferences.getString(PHONE_ID_KEY, null)
    }

    fun savePairingKey(pairingKey: String) {
        sharedPreferences.edit().putString(PAIRING_KEY, pairingKey).apply()
    }

    fun getPairingKey(): String? {
        return sharedPreferences.getString(PAIRING_KEY, null)
    }

    fun clearData() {
        sharedPreferences.edit().clear().apply()
    }

    fun generateSecureRandomKey(): String {
        val random = SecureRandom()
        val keyBytes = ByteArray(32) // 256-bit key
        random.nextBytes(keyBytes)
        return Base64.encodeToString(keyBytes, Base64.NO_WRAP)
    }
}
