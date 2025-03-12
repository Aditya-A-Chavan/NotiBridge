package com.example.notibridge.authentication.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "notibridge_prefs")

class PrefsManager(private val context: Context) {

    companion object {
        private val DEVICE_ID_KEY = stringPreferencesKey("device_id")
        private val HOSTNAME_KEY = stringPreferencesKey("hostname")
    }

    suspend fun saveDeviceId(deviceId: String) {
        context.dataStore.edit { prefs ->
            prefs[DEVICE_ID_KEY] = deviceId
        }
    }

    suspend fun getDeviceId(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[DEVICE_ID_KEY]
    }

    suspend fun saveHostname(hostname: String) {
        context.dataStore.edit { prefs ->
            prefs[HOSTNAME_KEY] = hostname
        }
    }

    suspend fun getHostname(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[HOSTNAME_KEY]
    }

    suspend fun clearData() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }

}