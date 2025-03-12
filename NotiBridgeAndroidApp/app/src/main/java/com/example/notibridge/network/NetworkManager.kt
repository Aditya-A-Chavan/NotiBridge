package com.example.notibridge.network

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

    suspend fun sendRequest(hostname: String, requestData: Map<String, String>): Map<String, Any?> {
        return withContext(Dispatchers.IO) {
            val socket = Socket()
            try {
                socket.connect(InetSocketAddress(hostname, 5001), TIMEOUT)
                val outputStream = OutputStreamWriter(socket.getOutputStream())
                val inputStream = BufferedReader(InputStreamReader(socket.getInputStream()))

                // Convert request data to JSON string
                val requestJson = JSONObject(requestData).toString()
                outputStream.write(requestJson + "\n")
                outputStream.flush()

                // Read response
                val responseJson = inputStream.readLine()
                socket.close()

                // Convert JSON response to Map<String, String>
                return@withContext JSONObject(responseJson).toMap()
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
