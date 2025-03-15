package com.example.notibridge.network.mdns

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log

class MdnsService(private val context: Context) {

    private var nsdManager: NsdManager? = null
    private var discoveryListener: NsdManager.DiscoveryListener? = null
    private var resolveListener: NsdManager.ResolveListener? = null

    fun startMdnsDiscovery(serviceType: String, onDeviceFound: (String, String) -> Unit) {
        nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager

        discoveryListener = object : NsdManager.DiscoveryListener {
            override fun onDiscoveryStarted(serviceType: String) {
                Log.d("mDNS", "Discovery started for $serviceType")
            }

            override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                Log.d("mDNS", "Service found: ${serviceInfo.serviceName}")

                resolveListener = object : NsdManager.ResolveListener {
                    override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                        Log.e("mDNS", "Resolve failed: $errorCode")
                    }

                    override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                        val ip = serviceInfo.host.hostAddress
                        val hostname = serviceInfo.serviceName
                        Log.d("mDNS", "Resolved: $hostname -> $ip")
                        onDeviceFound(ip, hostname)
                    }
                }

                nsdManager?.resolveService(serviceInfo, resolveListener!!)
            }

            override fun onServiceLost(serviceInfo: NsdServiceInfo) {
                Log.d("mDNS", "Service lost: ${serviceInfo.serviceName}")
            }

            override fun onDiscoveryStopped(serviceType: String) {
                Log.d("mDNS", "Discovery stopped")
            }

            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e("mDNS", "Discovery start failed: $errorCode")
                stopMdnsDiscovery()
            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                Log.e("mDNS", "Discovery stop failed: $errorCode")
            }
        }

        nsdManager?.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener!!)
    }

    fun stopMdnsDiscovery() {
        try {
            discoveryListener?.let { nsdManager?.stopServiceDiscovery(it) }
            resolveListener = null
            discoveryListener = null
            nsdManager = null
            Log.d("mDNS", "mDNS discovery stopped")
        } catch (e: Exception) {
            Log.e("mDNS", "Error stopping discovery: ${e.message}")
        }
    }
}
