import json
import socket
import qrcode
import logging
from qrcode.constants import ERROR_CORRECT_L
from threading import Thread
from zeroconf import IPVersion, ServiceInfo, Zeroconf

# Constants
DEVICE_ID = "didtest"
PORT = 5001
QR_CODE_FILE = "pairing.png"
SERVICE_TYPE = "_notibridge._tcp.local."
SERVICE_NAME = f"{DEVICE_ID}.{SERVICE_TYPE}"

# Configure Logging
logger = logging.getLogger("NotiBridge")
logger.setLevel(logging.DEBUG)

consoleHandler = logging.StreamHandler()
consoleHandler.setLevel(logging.DEBUG)  # Show all logs in terminal
formatter = logging.Formatter("%(asctime)s - %(levelname)s - %(message)s")
consoleHandler.setFormatter(formatter)
logger.addHandler(consoleHandler)


def get_local_ip():
    """Fetches the local IP address of the machine."""
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8", 80))
        local_ip = s.getsockname()[0]
        s.close()
        logger.info(f"Local IP address: {local_ip}")
        return local_ip
    except Exception as e:
        logger.error(f"Error getting local IP address: {e}")
        return "127.0.0.1"


def generate_qr_code(device_id, port):
    """Generates a QR code with pairing details and saves it as an image."""
    pairing_data = {"device_id": device_id, "port": port}
    pairing_json = json.dumps(pairing_data)
    qr = qrcode.QRCode(
        version=1,
        error_correction=ERROR_CORRECT_L,
        box_size=10,
        border=4,
    )
    qr.add_data(pairing_json)
    qr.make(fit=True)
    img = qr.make_image(fill_color="black", back_color="white")
    img.save(QR_CODE_FILE)
    logger.info(f"QR code saved to {QR_CODE_FILE}")


def start_socket_server_Auth(hostname, port):
    """Starts a persistent socket server to handle authentication requests."""
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.bind((hostname, port))
    server_socket.listen(5)

    logger.info(f"Socket server started on {hostname}:{port}")

    while True:
        logger.debug("Waiting for a connection...")
        client_socket, client_address = server_socket.accept()
        logger.info(f"Connection established with {client_address[0]}:{client_address[1]}")

        try:
            data = client_socket.recv(1024)
            if data:
                message = data.decode("utf-8")
                logger.info(f"Received data from {client_address[0]}:{client_address[1]} -> {message}")

                response = json.dumps({"status": "SUCCESS"})
                client_socket.send(response.encode("utf-8"))
                logger.info(f"Sent response: {response}")
            else:
                logger.warning(f"No data received from client {client_address[0]}:{client_address[1]}")

        except Exception as e:
            logger.error(f"Error handling client {client_address[0]}:{client_address[1]} - {e}")

        finally:
            client_socket.close()
            logger.info(f"Connection closed with {client_address[0]}:{client_address[1]}")


def advertise_mdns_service(hostname, port, device_id):
    """Advertises the device over mDNS for automatic discovery."""
    service_info = ServiceInfo(
        SERVICE_TYPE,
        SERVICE_NAME,
        addresses=[socket.inet_aton(hostname)],
        port=port,
        properties={"device_id": device_id.encode("utf-8")},
    )

    zeroconf = Zeroconf(ip_version=IPVersion.V4Only)
    zeroconf.register_service(service_info)

    logger.info(f"mDNS service advertised as {SERVICE_NAME} on {hostname}:{port}")

    try:
        while True:
            pass
    except KeyboardInterrupt:
        zeroconf.unregister_service(service_info)
        zeroconf.close()
        logger.info("mDNS service unregistered.")


def main():
    """Main function to start the socket server and mDNS service."""
    hostname = get_local_ip()
    generate_qr_code(DEVICE_ID, PORT)

    server_thread = Thread(target=start_socket_server_Auth, args=(hostname, PORT), daemon=True)
    server_thread.start()

    mdns_thread = Thread(target=advertise_mdns_service, args=(hostname, PORT, DEVICE_ID), daemon=True)
    mdns_thread.start()

    try:
        while True:
            pass
    except KeyboardInterrupt:
        logger.info("Server stopped.")


if __name__ == "__main__":
    main()
