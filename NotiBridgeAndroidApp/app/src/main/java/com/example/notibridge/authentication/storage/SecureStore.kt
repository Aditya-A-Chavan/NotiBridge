package com.example.notibridge.authentication.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey


class SecureStore(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )


    fun saveSecureData(key: String, value: String){
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getSecureData(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun clearSecureData(key: String){
        sharedPreferences.edit().remove(key).apply()
    }

}