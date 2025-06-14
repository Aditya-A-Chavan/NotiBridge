package org.aditya.notibridgedesktopappjava.authentication;

import org.aditya.notibridgedesktopappjava.util.DeviceIDUtil;
import org.aditya.notibridgedesktopappjava.util.SecureFileStorageUtil;
import org.junit.internal.matchers.Each;
import org.aditya.notibridgedesktopappjava.PairingState.StateManager;
import org.aditya.notibridgedesktopappjava.PairingState.PairingState;
import javafx.application.Platform;
import org.json.*;

public class PairingManager {
    // private static final String PAIRING_FILE = "pairing.json";
    private String currentPairingKey;

    public PairingManager() {
        this.currentPairingKey = CreateQRCode.getPairingKey();
    }

    public boolean verifyPairing(String deviceId, String phoneId, String pairingKey) {
        if (!deviceId.equals(DeviceIDUtil.getMACAddress())) {
            return false;
        }

        // if (!pairingKey.equals(currentPairingKey)) {
        //     return false;
        // }

        if (!SecureFileStorageUtil.isDataStored()){
            if(!pairingKey.equals(currentPairingKey)){
                return false;
            } else {
                return true;
            }
        }else {
            try{
            String pairedDeviceData = SecureFileStorageUtil.loadDecryptedData();
            JSONObject pairedDeviceDataJSON = new JSONObject(pairedDeviceData);
            String key = pairedDeviceDataJSON.getString("key");

            if (!pairingKey.equals(key)){
                return false;
            }
            else {
                return true;
            }

            } catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

    }

    public boolean pairDevice(String deviceId, String phoneId, String pairingKey) {
        if (verifyPairing(deviceId, phoneId, pairingKey)){
            try{
                savePairingInfo(phoneId, pairingKey);
                Platform.runLater(() -> {
                    StateManager.getInstance().setState(PairingState.PAIRED_CONNECTED);
                });
                return true;
            } catch (Exception e){
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }


    //OLDAPPROACH:
    // private void savePairingInfo(String phoneId) throws IOException {
    //     JSONObject pairingInfo = new JSONObject();
    //     pairingInfo.put("phone_id", phoneId);
    //     pairingInfo.put("device_id", DeviceIDUtil.getMACAddress());
    //     pairingInfo.put("paired_at", System.currentTimeMillis());

    //     try (FileWriter file = new FileWriter(PAIRING_FILE)) {
    //         file.write(pairingInfo.toString(2));
    //     }
    // }

    //NEWAPPROACH (Encrypted):

    private void savePairingInfo(String phoneId, String currentPairingKey) throws Exception {
        
            if(!SecureFileStorageUtil.isDataStored()){
                String pairingInfo = "{ \"phoneId\": \"" + phoneId + "\", \"key\": \"" + currentPairingKey + "\", \"pairedAt\": " + System.currentTimeMillis() + " }";
                SecureFileStorageUtil.saveEncryptedData(pairingInfo);
                System.out.println("Pairing Data Stored");
                SecureFileStorageUtil.loadDecryptedData();
            }else{
                SecureFileStorageUtil.clearData();
                System.out.println("Pairing Data cleared");
                String pairingInfo = "{ \"phoneId\": \"" + phoneId + "\", \"key\": \"" + currentPairingKey + "\", \"pairedAt\": " + System.currentTimeMillis() + " }";
                SecureFileStorageUtil.saveEncryptedData(pairingInfo);
                System.out.println(SecureFileStorageUtil.loadEncryptedData());
                System.out.println("Pairing Data Stored");
                System.out.println(SecureFileStorageUtil.loadDecryptedData());
            }
        
    }

    // public String getPairedPhoneId() {
    //     try {
    //         if (Files.exists(Paths.get(PAIRING_FILE))) {
    //             String content = new String(Files.readAllBytes(Paths.get(PAIRING_FILE)));
    //             JSONObject pairingInfo = new JSONObject(content);
    //             return pairingInfo.getString("phone_id");
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     return null;
    // }

    public String getPairedDetails(){
        try{
            if(SecureFileStorageUtil.isDataStored()){
                return SecureFileStorageUtil.loadDecryptedData();  
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean unpairDevice(String deviceId, String phoneId, String pairingKey){
        try{
            if(!SecureFileStorageUtil.isDataStored()){
                System.err.println("No Device is Paired");
                return false;
            }
            else{
                verifyPairing(deviceId, phoneId, pairingKey);
                SecureFileStorageUtil.clearData();
                Platform.runLater(() -> {
                    StateManager.getInstance().setState(PairingState.UNPAIRED);
                });
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean authenticatePairedDevice(String deviceId, String phoneId, String pairingKey){
        try{
            if(!SecureFileStorageUtil.isDataStored()){
                System.err.println("No device paired but recieved authentication request, if it is you, please force pair device again");
                return false;
            }
           else{
                if(verifyPairing(deviceId, phoneId, pairingKey)){
                    Platform.runLater(() -> {
                        StateManager.getInstance().setState(PairingState.PAIRED_CONNECTED);
                    });
                    System.out.println("authenticatePairedDevice, verified");
                    return true;
                }else{
                    return false;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
} 