package com.example.notibridge.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.InetSocketAddress
import java.net.Socket
import org.json.JSONObject

class SocketConnectionManager {
    private var socket: Socket? = null
    private var outputStream: OutputStreamWriter? = null
    private var inputStream: BufferedReader? = null
    private val scope = CoroutineScope(Dispatchers.IO)
    
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    enum class ConnectionState {
        CONNECTED,
        DISCONNECTED,
        CONNECTING
    }

    suspend fun connect(hostIp: String, port: Int = 5002): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                _connectionState.value = ConnectionState.CONNECTING
                
                // Close existing connection if any
                disconnect()
                
                // Create new socket connection
                socket = Socket()
                socket?.connect(InetSocketAddress(hostIp, port), 5000)
                
                outputStream = OutputStreamWriter(socket?.getOutputStream())
                inputStream = BufferedReader(InputStreamReader(socket?.getInputStream()))
                
                // Start listening for messages
                startMessageListener()
                
                _connectionState.value = ConnectionState.CONNECTED
                Log.d("SocketConnectionManager", "Connected to $hostIp:$port")
                true
            } catch (e: Exception) {
                Log.e("SocketConnectionManager", "Connection failed: ${e.message}")
                disconnect()
                _connectionState.value = ConnectionState.DISCONNECTED
                false
            }
        }
    }

    private fun startMessageListener() {
        scope.launch {
            try {
                while (socket?.isConnected == true) {
                    val message = inputStream?.readLine()
                    if (message == null) {
                        // Connection closed
                        disconnect()
                        break
                    }
                    // Handle incoming message
                    handleIncomingMessage(message)
                }
            } catch (e: Exception) {
                Log.e("SocketConnectionManager", "Error reading message: ${e.message}")
                disconnect()
            }
        }
    }

    private fun handleIncomingMessage(message: String) {
        // TODO: Implement message handling logic
        Log.d("SocketConnectionManager", "Received message: $message")
    }

    suspend fun sendMessage(data: Map<String, Any?>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (socket?.isConnected != true) {
                    Log.e("SocketConnectionManager", "Socket not connected")
                    return@withContext false
                }

                val jsonMessage = JSONObject(data).toString()
                outputStream?.write("$jsonMessage\n")
                outputStream?.flush()
                true
            } catch (e: Exception) {
                Log.e("SocketConnectionManager", "Error sending message: ${e.message}")
                disconnect()
                false
            }
        }
    }

    fun disconnect() {
        try {
            inputStream?.close()
            outputStream?.close()
            socket?.close()
        } catch (e: Exception) {
            Log.e("SocketConnectionManager", "Error closing connection: ${e.message}")
        } finally {
            inputStream = null
            outputStream = null
            socket = null
            _connectionState.value = ConnectionState.DISCONNECTED
        }
    }
} 