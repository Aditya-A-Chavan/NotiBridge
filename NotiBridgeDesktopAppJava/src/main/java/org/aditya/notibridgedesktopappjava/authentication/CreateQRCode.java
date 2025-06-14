package org.aditya.notibridgedesktopappjava.authentication;
import java.io.IOException;
import org.json.JSONObject;

import org.aditya.notibridgedesktopappjava.util.DeviceIDUtil;
import org.aditya.notibridgedesktopappjava.util.QRCodeGenerator;

import com.google.zxing.WriterException;

public class CreateQRCode {
    private static final String device_id = DeviceIDUtil.getMACAddress();
    private static final String pairing_key = generatePairingKey();
    private static final String hostname = "localhost"; // This will be replaced with actual hostname
    
    private static String generatePairingKey() {
        return String.format("%06d", (int)(Math.random() * 1000000));
    }
    
    public static void generateQRCode() throws IOException, WriterException {
        JSONObject qrData = new JSONObject();
        qrData.put("device_id", device_id);
        qrData.put("pairing_key", pairing_key);
        qrData.put("hostname", hostname);
        
        QRCodeGenerator.generateQRCode(qrData.toString());
    }

    public static String getQRCodePath() {
        return QRCodeGenerator.getQRCodePath();
    }
    
    public static String getPairingKey() {
        return pairing_key;
    }
} 