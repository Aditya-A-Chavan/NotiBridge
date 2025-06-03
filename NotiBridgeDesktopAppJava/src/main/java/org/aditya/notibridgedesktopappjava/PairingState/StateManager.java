package org.aditya.notibridgedesktopappjava.PairingState;

public class StateManager {
    private static StateManager instance;
    private PairingState currentState;
    private StateChangeListener stateChangeListener;

    public interface StateChangeListener {
        void onStateChanged(PairingState newState);
    }

    private StateManager() {
        //ALWAYS START WITH UNPAIRED STATE
        currentState = PairingState.UNPAIRED;
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

    public String getStateMessage() {
        switch (currentState) {
            case PAIRED_CONNECTED:
                return "Device is paired and connected";
            case PAIRED_DISCONNECTED:
                return "Device is paired but disconnected";
            case UNPAIRED:
                return "Device is not paired";
            default:
                return "Unknown state";
        }
    }
}
