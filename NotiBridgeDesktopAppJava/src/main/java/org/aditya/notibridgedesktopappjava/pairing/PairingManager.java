package org.aditya.notibridgedesktopappjava.pairing;

import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.aditya.notibridgedesktopappjava.util.DeviceIDUtil;

public class PairingManager {
    private static final String PAIRING_FILE = "pairing.json";
    private String currentPairingKey;

    public PairingManager() {
        this.currentPairingKey = CreateQRCode.getPairingKey();
    }

    public boolean verifyPairing(String deviceId, String phoneId, String pairingKey) {
        if (!deviceId.equals(DeviceIDUtil.getMACAddress())) {
            return false;
        }

        if (!pairingKey.equals(currentPairingKey)) {
            return false;
        }

        try {
            savePairingInfo(phoneId);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void savePairingInfo(String phoneId) throws IOException {
        JSONObject pairingInfo = new JSONObject();
        pairingInfo.put("phone_id", phoneId);
        pairingInfo.put("device_id", DeviceIDUtil.getMACAddress());
        pairingInfo.put("paired_at", System.currentTimeMillis());

        try (FileWriter file = new FileWriter(PAIRING_FILE)) {
            file.write(pairingInfo.toString(2));
        }
    }

    public String getPairedPhoneId() {
        try {
            if (Files.exists(Paths.get(PAIRING_FILE))) {
                String content = new String(Files.readAllBytes(Paths.get(PAIRING_FILE)));
                JSONObject pairingInfo = new JSONObject(content);
                return pairingInfo.getString("phone_id");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
} 