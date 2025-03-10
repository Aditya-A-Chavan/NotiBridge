package com.example.notibridge.authentication.storage

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.net.ssl.HostnameVerifier


private val Context.dataStore by preferencesDataStore("prefs")

class PrefsManager(private val context: Context){

    companion object{
        private val DEVICE_ID = stringPreferencesKey("device_id")
        private val HOSTNAME = stringPreferencesKey("hostname")
        private val IS_PAIRED = booleanPreferencesKey("is_paired")
    }

    suspend fun savePairingData(deviceId: String, hostname: String) {
        context.dataStore.edit { prefs ->
            prefs[DEVICE_ID] = deviceId
            prefs[HOSTNAME] = hostname
            prefs[IS_PAIRED] = true
        }
    }


    suspend fun clearPairingData(){
        context.dataStore.edit { prefs ->
            prefs.remove(DEVICE_ID)
            prefs.remove(HOSTNAME)
            prefs[IS_PAIRED] = false
        }
    }

    suspend fun getPairingData(): Pair<String?, String?> {
        val prefs = context.dataStore.data.first()
        return prefs[DEVICE_ID] to prefs[HOSTNAME]
    }

    suspend fun isPaired(): Boolean{
        return context.dataStore.data.map {it[IS_PAIRED] ?: false}.first()
    }
}