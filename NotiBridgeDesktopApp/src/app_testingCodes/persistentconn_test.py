import json
import socket
from threading import Thread
from zeroconf import IPVersion, ServiceInfo, Zeroconf
import logging

# Configure Logging
logger = logging.getLogger("persistentConnectionLogger")
logger.setLevel(logging.DEBUG)

consoleHandler = logging.StreamHandler()
consoleHandler.setLevel(logging.DEBUG)  # Lowest level prints all logs
formatter = logging.Formatter("%(asctime)s - %(levelname)s - %(message)s")
consoleHandler.setFormatter(formatter)
logger.addHandler(consoleHandler)


def handle_client(client_socket, client_address):
    """Handles communication with a connected client."""
    logger.info("Handling connection with %s:%d", client_address[0], client_address[1])

    try:
        while True:
            data = client_socket.recv(1024)  # Receive data (buffer size = 1024 bytes)
            if not data:
                logger.info("Client %s:%d disconnected", client_address[0], client_address[1])
                break  # Exit the loop if the client disconnects

            # Decode the message (assuming JSON format)
            try:
                message = json.loads(data.decode("utf-8"))
                logger.info("Received data from %s:%d -> %s", client_address[0], client_address[1], message)

                # Example response (Echo the message back)
                response = {"status": "success", "received": message}
                client_socket.send(json.dumps(response).encode("utf-8"))

            except json.JSONDecodeError:
                logger.warning("Invalid JSON received from %s:%d", client_address[0], client_address[1])
                client_socket.send(json.dumps({"error": "Invalid JSON format"}).encode("utf-8"))

    except ConnectionResetError:
        logger.warning("Connection forcibly closed by client: %s:%d", client_address[0], client_address[1])

    finally:
        client_socket.close()  # Ensure the socket is closed


def startSocketServerPersistent(hostip, port):
    """Starts a persistent TCP socket server."""
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)  # Allows quick reuse of the port
    server_socket.bind((hostip, port))
    server_socket.listen(5)  # Allow up to 5 clients in the queue

    logger.info("Persistent Connection Socket started at %s:%d", hostip, port)

    try:
        while True:
            logger.debug("Waiting for a connection...")
            client_socket, client_address = server_socket.accept()
            logger.info("Connection Established with %s:%d", client_address[0], client_address[1])

            # Handle each client in a separate thread
            client_thread = Thread(target=handle_client, args=(client_socket, client_address))
            client_thread.start()

    except KeyboardInterrupt:
        logger.info("Shutting down server...")

    finally:
        server_socket.close()
        logger.info("Server socket closed.")
