package org.aditya.notibridgedesktopappjava.mdns;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.DatagramSocket;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.aditya.notibridgedesktopappjava.util.DeviceIDUtil;

public class MDNSService {
    private static final String SERVICE_TYPE = "_notibridge._tcp.local.";
    private static final int PORT = 5001;
    private JmDNS jmdns;
    private static final String serviceName = DeviceIDUtil.getMACAddress();

    // public MDNSService() {
    //     String deviceId = DeviceIDUtil.getMACAddress();
    //     this.serviceName = deviceId + "." + SERVICE_TYPE;
    // }

    private String getLocalIpAddress() {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 80);
            String localIp = socket.getLocalAddress().getHostAddress();
            System.out.println("Local IP address: " + localIp);
            return localIp;
        } catch (Exception e) {
            e.printStackTrace();
            return "127.0.0.1";
        }
    }

    public void startBroadcasting() {
        try {
            String localIp = getLocalIpAddress();
            
            jmdns = JmDNS.create(InetAddress.getByName(localIp));
            
            Map<String, String> properties = new HashMap<>();
            properties.put("device_id", DeviceIDUtil.getMACAddress());
            
            ServiceInfo serviceInfo = ServiceInfo.create(
                SERVICE_TYPE,
                serviceName,
                PORT,
                0, 
                0,
                true,
                properties
            );
            
            jmdns.registerService(serviceInfo);
            System.out.println("mDNS service started: " + serviceName + " on " + localIp + ":" + PORT);
            System.out.println("Service properties: " + properties);
        } catch (IOException e) {
            System.err.println("Error starting mDNS service: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopBroadcasting() {
        if (jmdns != null) {
            try {
                jmdns.unregisterAllServices();
                jmdns.close();
                System.out.println("mDNS service stopped");
            } catch (IOException e) {
                System.err.println("Error stopping mDNS service: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
} 