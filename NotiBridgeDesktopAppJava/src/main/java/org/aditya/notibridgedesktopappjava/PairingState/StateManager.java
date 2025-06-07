package org.aditya.notibridgedesktopappjava.PairingState;

import org.aditya.notibridgedesktopappjava.util.SecureFileStorageUtil;
import org.aditya.notibridgedesktopappjava.mdns.MDNSService;

public class StateManager {
    private static StateManager instance;
    private PairingState currentState;
    private StateChangeListener stateChangeListener;
    private MDNSService mdnsService;

    public interface StateChangeListener {
        void onStateChanged(PairingState newState);
    }

    private StateManager() {
        mdnsService = new MDNSService();
        updateState();
    }

    public static StateManager getInstance() {
        if (instance == null) {
            instance = new StateManager();
        }
        return instance;
    }

    public void setStateChangeListener(StateChangeListener listener) {
        this.stateChangeListener = listener;
    }

    public PairingState getCurrentState() {
        return currentState;
    }

    public void setState(PairingState newState) {
        if (this.currentState != newState) {
            this.currentState = newState;
            if (stateChangeListener != null) {
                stateChangeListener.onStateChanged(newState);
            }
        }
    }

    public void updateState() {
        if (!SecureFileStorageUtil.isDataStored()) {
            setState(PairingState.UNPAIRED);
            mdnsService.stopBroadcasting();
        } else {
            // If file exists but no device is connected, start MDNS broadcasting
            setState(PairingState.PAIRED_DISCONNECTED);
            mdnsService.startBroadcasting();
        }
    }

    public void setDeviceConnected(boolean connected) {
        if (SecureFileStorageUtil.isDataStored()) {
            if (connected) {
                setState(PairingState.PAIRED_CONNECTED);
                mdnsService.stopBroadcasting();
            } else {
                setState(PairingState.PAIRED_DISCONNECTED);
                mdnsService.startBroadcasting();
            }
        }
    }

    public String getStateMessage() {
        switch (currentState) {
            case PAIRED_CONNECTED:
                return "Device is paired and connected";
            case PAIRED_DISCONNECTED:
                return "Device is paired but disconnected. Searching for device...";
            case UNPAIRED:
                return "Device is not paired. Please scan the QRCODE displayed below using the NotiBridge Android app to initiate the pairing process";
            default:
                return "Unknown state";
        }
    }
}
