import json
import socket
import qrcode
from qrcode.constants import ERROR_CORRECT_L
from threading import Thread

# Configuration
DEVICE_ID = "DIDTEST"  # Replace with actual device ID
PORT = 5001  # Port for socket server
QR_CODE_FILE = "pairing_qr.png"  # Output file for QR code

# Function to get the local IP address
def get_local_ip():
    try:
        # Create a socket to get the local IP address
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8", 80))  # Connect to a public DNS server
        local_ip = s.getsockname()[0]  # Get the local IP address
        s.close()
        return local_ip
    except Exception as e:
        print(f"Error getting local IP address: {e}")
        return "127.0.0.1"  # Fallback to localhost if unable to determine IP

# Generate QR Code
def generate_qr_code(device_id, hostname, port):
    # Create a dictionary with pairing data
    pairing_data = {
        "device_id": device_id,
        "hostname": hostname,
        "port": port,
    }

    # Convert the dictionary to a JSON string
    pairing_json = json.dumps(pairing_data)

    # Generate the QR code
    qr = qrcode.QRCode(
        version=1,
        error_correction=ERROR_CORRECT_L,
        box_size=10,
        border=4,
    )
    qr.add_data(pairing_json)
    qr.make(fit=True)

    # Create an image from the QR code
    img = qr.make_image(fill_color="black", back_color="white")

    # Save the image to a file
    img.save(QR_CODE_FILE)
    print(f"QR code saved to {QR_CODE_FILE}")

# Socket Server
def start_socket_server(hostname, port):
    # Create a socket object
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind((hostname, port))
    server_socket.listen(1)
    print(f"Socket server started on {hostname}:{port}")

    while True:
        # Wait for a connection
        print("Waiting for a connection...")
        client_socket, client_address = server_socket.accept()
        print(f"Connection established with {client_address}")

        try:
            # Receive data from the client
            data = client_socket.recv(1024)
            if data:
                print(f"Received data: {data.decode('utf-8')}")
                # Send a response back to the client
                response = {"Response": "Connection successful!"}
                client_socket.send(response)
            else:
                print("No data received from client.")
        except Exception as e:
            print(f"Error: {e}")
        finally:
            # Close the connection
            client_socket.close()
            print("Connection closed.")

# Main Function
def main():
    # Get the local IP address
    hostname = get_local_ip()
    print(f"Local IP address: {hostname}")

    # Generate the QR code
    generate_qr_code(DEVICE_ID, hostname, PORT)

    # Start the socket server in a separate thread
    server_thread = Thread(target=start_socket_server, args=(hostname, PORT))
    server_thread.daemon = True
    server_thread.start()

    # Keep the main thread alive
    try:
        while True:
            pass
    except KeyboardInterrupt:
        print("Server stopped.")

if __name__ == "__main__":
    main()