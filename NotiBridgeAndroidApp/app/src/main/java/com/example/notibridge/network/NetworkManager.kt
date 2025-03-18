package com.example.notibridge.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.InetSocketAddress
import java.net.Socket
import org.json.JSONObject

class NetworkManager {

    private val TIMEOUT = 5000  // 5 seconds timeout
    private val gson = Gson()

    suspend fun sendRequest(hostIp: String, requestData: Map<String, String>): Map<String, Any?> {
        Log.d("NetworkManager.sendRequest", "Network Manager's send req called on $hostIp")
        return withContext(Dispatchers.IO) {
            val socket = Socket()

            try {
                socket.connect(InetSocketAddress(hostIp, 5001), TIMEOUT)
                val outputStream = OutputStreamWriter(socket.getOutputStream())
                val inputStream = BufferedReader(InputStreamReader(socket.getInputStream()))

                // Convert request data to JSON string
                val requestJson = JSONObject(requestData).toString()
                outputStream.write(requestJson + "\n")
                outputStream.flush()

                // Read response
                val responseJson = inputStream.readLine()
                socket.close()

                val type = object : TypeToken<Map<String, Any?>>() {}.type
                return@withContext gson.fromJson(responseJson, type)
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext mapOf(
                    "status" to "ERROR",
                    "message" to e.localizedMessage
                )
            } finally {
                socket.close()
            }
        }
    }

    // Extension function for JSONObject
    private fun JSONObject.toMap(): Map<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        val keys = this.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            map[key] = this.get(key)
        }
        return map
    }
}