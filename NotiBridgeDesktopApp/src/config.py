import os
import uuid
import json

APP_NAME = "NotiBridge"
BASE_DIR = os.path.join(os.getenv("APPDATA"), APP_NAME)  # C:\Users\<Username>\AppData\Roaming\NotiBridge
DATA_DIR = os.path.join(BASE_DIR, "data")
LOGS_DIR = os.path.join(BASE_DIR, "logs")
CONFIG_FILE = os.path.join(DATA_DIR, "config.json")

os.makedirs(DATA_DIR, exist_ok=True)
os.makedirs(LOGS_DIR, exist_ok=True)


DEFAULT_CONFIG = {
    "deviceId": str(uuid.uuid4()),  # Generate a unique device ID
    "websocket_port": 5001,
    "tcp_port": 5002,
    "mdns_service_name": "_notibridge._tcp.local",
    "log_file": os.path.join(LOGS_DIR, "notibridge.log"),
    "error_log_file": os.path.join(LOGS_DIR, "error.log"),
    "paired_devices_file": os.path.join(DATA_DIR, "paired_devices.json"),
    "pairing_state_file": os.path.join(DATA_DIR, "pairing_state.json")
}


def load_config():
    if not os.path.exists(CONFIG_FILE):
        with open(CONFIG_FILE, "w") as f:
            json.dump(DEFAULT_CONFIG, f, indent=4)
        return DEFAULT_CONFIG
    with open(CONFIG_FILE, "r") as f:
        return json.load(f)


CONFIG = load_config()

DEVICE_ID = CONFIG["deviceId"]
WEBSOCKET_PORT = CONFIG["websocket_port"]
TCP_PORT = CONFIG["tcp_port"]
MDNS_SERVICE_NAME = CONFIG["mdns_service_name"]
LOG_FILE = CONFIG["log_file"]
ERROR_LOG_FILE = CONFIG["error_log_file"]
PAIRED_DEVICES_FILE = CONFIG["paired_devices_file"]
PAIRING_STATE_FILE = CONFIG["pairing_state_file"]
