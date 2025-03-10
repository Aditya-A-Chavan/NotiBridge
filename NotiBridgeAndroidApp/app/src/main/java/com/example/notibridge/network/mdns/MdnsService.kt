package com.example.notibridge.network.mdns

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo
import javax.jmdns.ServiceListener
import java.net.InetAddress
import java.net.NetworkInterface

class MdnsService(private val context: Context) {

    private var jmDNS: JmDNS? = null

    suspend fun discoverDesktop(deviceId: String, onDeviceFound: (String, String) -> Unit) {
        withContext(Dispatchers.IO) {
            try {
                val localIp = getLocalIpAddress()
                if (localIp == null) {
                    Log.e("MdnsService", "No valid local IP found.")
                    return@withContext
                }

                jmDNS = JmDNS.create(InetAddress.getByName(localIp))
                val serviceType = "_notibridge._tcp.local.$deviceId"

                jmDNS?.addServiceListener(serviceType, object : ServiceListener {
                    override fun serviceAdded(event: javax.jmdns.ServiceEvent) {
                        Log.d("MdnsService", "Service added: ${event.info}")
                        jmDNS?.requestServiceInfo(serviceType, event.name)
                    }

                    override fun serviceRemoved(event: javax.jmdns.ServiceEvent) {
                        Log.d("MdnsService", "Service removed: ${event.info}")
                    }

                    override fun serviceResolved(event: javax.jmdns.ServiceEvent) {
                        val serviceInfo: ServiceInfo = event.info
                        val hostname = serviceInfo.server
                        val ip = serviceInfo.inetAddresses.firstOrNull()?.hostAddress ?: "Unknown"

                        Log.d("MdnsService", "Service resolved: Hostname: $hostname, IP: $ip")
                        onDeviceFound(hostname, ip)
                    }
                })

            } catch (e: Exception) {
                Log.e("MdnsService", "Error discovering mDNS service: ${e.message}")
            }
        }
    }

    private fun getLocalIpAddress(): String? {
        return try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            for (intf in interfaces) {
                val addresses = intf.inetAddresses
                for (addr in addresses) {
                    if (!addr.isLoopbackAddress) {
                        return addr.hostAddress
                    }
                }
            }
            null
        } catch (e: Exception) {
            Log.e("MdnsService", "Error getting local IP: ${e.message}")
            null
        }
    }

    fun stopDiscovery() {
        jmDNS?.close()
    }
}
