package org.aditya.notibridgedesktopappjava.config;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class DeviceIDUtil {
    public static String getMACAddress(){
        try{
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while(networks.hasMoreElements()){
                NetworkInterface netInt = networks.nextElement();
                byte[] mac = netInt.getHardwareAddress();
                if(mac != null && mac.length > 0){
                    StringBuilder sb = new StringBuilder();
                    for(byte b : mac){
                        sb.append(String.format("%02X", b));
                    }
                    return sb.toString();
                }
            }
        }catch(SocketException e){
            e.printStackTrace();
        }
        return "UNKNOWN";
    }
}
