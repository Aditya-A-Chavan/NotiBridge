import tkinter as tk
from tkinter import ttk
import time
import threading
from server.pairingManager import PairingStateManager
from utils.logger import logger
# from ui.qr_display import generate_qr_code

class NotiBridgeUI:
    def __init__(self):
        """Initialize UI components and set up state-based UI changes."""
        self.root = tk.Tk()
        self.root.title("NotiBridge Desktop")
        self.root.geometry("400x400")
        self.root.resizable(False, False)

        self.label = tk.Label(self.root, text="Loading...", font=("Arial", 14))
        self.label.pack(pady=20)

        self.qr_label = tk.Label(self.root)
        self.qr_label.pack()

        self.loader = ttk.Progressbar(self.root, mode="indeterminate")

        # Start the pairing state manager to listen for changes
        self.pairing_manager = PairingStateManager(self.on_pairing_state_changed)
        self.pairing_manager.start()

    def on_pairing_state_changed(self, new_state):
        """Dynamically update UI based on pairing state changes."""
        self.clear_ui()

        if new_state == "UNPAIRED":
            self.show_qr_code()
        elif new_state == "PAIRED_DISCONNECTED":
            self.show_loader("NotiBridge Desktop is now discoverable.\nTrying to reconnect...")
        elif new_state == "PAIRED_CONNECTED":
            self.show_connected_message()

    def show_qr_code(self):
        logger.debug("QR CODE IMPPLEMENTATION PENDING")
    #     """Displays a QR code for pairing."""
    #     self.label.config(text="Scan this QR Code to pair")
    #     qr_image = generate_qr_code()
    #     self.qr_label.config(image=qr_image)
    #     self.qr_label.image = qr_image  # Prevent garbage collection

    def show_loader(self, message):
        """Displays a loading animation with a message."""
        self.label.config(text=message)
        self.loader.pack(pady=20)
        self.loader.start()

    def show_connected_message(self):
        """Displays a simple 'CONNECTED' message."""
        self.label.config(text="CONNECTED", font=("Arial", 16, "bold"))

    def clear_ui(self):
        """Removes all UI elements before updating."""
        self.label.pack_forget()
        self.qr_label.pack_forget()
        self.loader.pack_forget()

        self.label.pack(pady=20)  # Re-add the label

    def run(self):
        """Starts the UI loop."""
        self.root.mainloop()

if __name__ == "__main__":
    ui = NotiBridgeUI()
    ui.run()
