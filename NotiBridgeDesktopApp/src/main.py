# import time
# import utils.logger

from server.pairingManager import PairingStateManager
# from server.mdns_handler import MDNSHandler
# from server.websocket_server import WebSocketServer
# from server.auth_manager import AuthManager
from ui.app import NotiBridgeUI



def start_ui():
    """Starts the UI on the main thread."""
    ui = NotiBridgeUI()
    ui.run()

if __name__ == "__main__":
    PairingStateManager.start()

    start_ui()