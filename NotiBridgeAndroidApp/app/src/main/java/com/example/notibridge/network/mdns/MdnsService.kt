package com.example.notibridge.network.mdns

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceInfo
import javax.jmdns.ServiceListener

class MdnsService(private val context: Context) {

    private var jmdns: JmDNS? = null

    suspend fun startMdnsDiscovery(serviceType: String, onDeviceFound: (String, String) -> Unit) {
        withContext(Dispatchers.IO) {
            try {
                val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as android.net.wifi.WifiManager
                val lock = wifiManager.createMulticastLock("mDNSLock")
                lock.setReferenceCounted(true)
                lock.acquire()

                val intf = InetAddress.getLocalHost()
                jmdns = JmDNS.create(intf)

                jmdns?.addServiceListener(serviceType, object : ServiceListener {
                    override fun serviceAdded(event: ServiceEvent) {
                        jmdns?.requestServiceInfo(event.type, event.name)
                    }

                    override fun serviceRemoved(event: ServiceEvent) {}

                    override fun serviceResolved(event: ServiceEvent) {
                        val ip = event.info.inetAddresses.firstOrNull()?.hostAddress
                        val hostname = event.info.name
                        if (ip != null) {
                            onDeviceFound(ip, hostname)
                        }
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun resolveDeviceHostname(deviceId: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val serviceType = "_notibridge._tcp.local."
                val serviceName = "$deviceId.$serviceType"
                val serviceInfo: ServiceInfo? = jmdns?.getServiceInfo(serviceType, serviceName)

                return@withContext serviceInfo?.inetAddresses?.firstOrNull()?.hostAddress
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            }
        }
    }

    fun stopMdnsDiscovery() {
        jmdns?.close()
        jmdns = null
    }
}
