import json
import os
from enum import Enum
from utils.logger import logger
from NotiBridgeDesktopApp.src.config import PAIRING_STATE_FILE
import time
import threading

class PairingStateManager:
    def __init__(self, callback):
        self.pairing_state = "UNPAIRED"
        self.callback = callback
        self.lock = threading.Lock()
        self.running = True
        self.load_pairing_state()
    
    def load_pairing_state(self):
            try:
                with open(PAIRING_STATE_FILE, "r") as f:
                    self.pairing_state = json.load(f).get("pairing_state", "UNPAIRED")
            except (json.JSONDecodeError, IOError) as e:
                logger.error(f"Error loading pairing state: {e}")
                self.pairing_state = "UNPAIRED"
        
        logger.info(f"Loaded pairing state: {self.pairing_state}")

    def save_pairing_state(self, newState):
        with self.lock:
            if self.pairing_state != newState:
                self.pairing_state = newState
                with open(PAIRING_STATE_FILE, "w") as f:
                    json.dump({"pairing_state": newState}, f)
                logger.info(f"Saved pairing state: {newState}")
                self.callback(newState)
    
    def monitorPairingState(self):
        logger.info("Pairing state monitor started")
        while self.running:
            time.sleep(5)
            self.load_pairing_state()

    
    def start(self):
        """Starts the pairing state monitoring in a separate thread."""

        self.monitorThread = threading.Thread(target=self.monitorPairingState, daemon=True)
        self.monitorThread.start()
    
    def stop(self):
        """"Stops the pairing state monitoring."""
        self.running = False
        self.monitorThread.join()
        logger.info("Pairing state monitor stopped")
