import logging
import os
from logging.handlers import RotatingFileHandler

LOG_DIR = os.path.join(os.getenv('APPDATA'), 'NotiBridge', "logs")
os.makedirs(LOG_DIR, exist_ok=True)

LOG_FILE = os.path.join(LOG_DIR, "notibridge.log")
ERROR_LOG_FILE = os.path.join(LOG_DIR, "notibridge_error.log")

logger = logging.getLogger("NotibridgeLogger")
logger.setLevel(logging.DEBUG)

log_formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s', datefmt='%Y-%m-%d %H:%M:%S')

file_handler = RotatingFileHandler(LOG_FILE, maxBytes=5 * 1024 * 1024, backupCount=3)  
file_handler.setLevel(logging.DEBUG)
file_handler.setFormatter(log_formatter)

# File handler for error logs
error_handler = RotatingFileHandler(ERROR_LOG_FILE, maxBytes=2 * 1024 * 1024, backupCount=2)
error_handler.setLevel(logging.ERROR)
error_handler.setFormatter(log_formatter)


console_handler = logging.StreamHandler()
console_handler.setLevel(logging.INFO)
console_handler.setFormatter(log_formatter)


logger.addHandler(file_handler)
logger.addHandler(error_handler)
logger.addHandler(console_handler)
