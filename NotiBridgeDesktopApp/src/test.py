import json
import socket
import qrcode
from qrcode.constants import ERROR_CORRECT_L
from threading import Thread
from zeroconf import IPVersion, ServiceInfo, Zeroconf

DEVICE_ID = "didtest"
PORT = 5001
QR_CODE_FILE = "pairing.png"
SERVICE_TYPE = "_notibridge._tcp.local."
SERVICE_NAME = f"{DEVICE_ID}.{SERVICE_TYPE}"

def get_local_ip():
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8", 80))
        local_ip = s.getsockname()[0]
        s.close()
        return local_ip
    except Exception as e:
        print(f"Error getting local IP address: {e}")
        return "127.0.0.1"

def generate_qr_code(device_id, port):
    pairing_data = {
        "device_id": device_id,
        "port": port,
    }
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
    print(f"QR code saved to {QR_CODE_FILE}")

def start_socket_server(hostname, port):
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind((hostname, port))
    server_socket.listen(1)
    print(f"Socket server started on {hostname}:{port}")
    while True:
        print("Waiting for a connection...")
        client_socket, client_address = server_socket.accept()
        print(f"Connection established with {client_address}")
        try:
            data = client_socket.recv(1024)
            if data:
                print(f"Received data: {data.decode('utf-8')}")
                response = json.dumps({"status": "SUCCESS"})
                client_socket.send(response.encode("utf-8"))
            else:
                print("No data received from client.")
        except Exception as e:
            print(f"Error: {e}")
        finally:
            client_socket.close()
            print("Connection closed.")

def advertise_mdns_service(hostname, port, device_id):
    service_info = ServiceInfo(
        SERVICE_TYPE,
        SERVICE_NAME,
        addresses=[socket.inet_aton(hostname)],
        port=port,
        properties={"device_id": device_id.encode("utf-8")},
    )
    zeroconf = Zeroconf(ip_version=IPVersion.V4Only)
    zeroconf.register_service(service_info)
    print(f"mDNS service advertised as {SERVICE_NAME} on {hostname}:{port}")
    try:
        while True:
            pass
    except KeyboardInterrupt:
        zeroconf.unregister_service(service_info)
        zeroconf.close()
        print("mDNS service unregistered.")

def main():
    hostname = get_local_ip()
    print(f"Local IP address: {hostname}")
    generate_qr_code(DEVICE_ID, PORT)
    server_thread = Thread(target=start_socket_server, args=(hostname, PORT))
    server_thread.daemon = True
    server_thread.start()
    mdns_thread = Thread(target=advertise_mdns_service, args=(hostname, PORT, DEVICE_ID))
    mdns_thread.daemon = True
    mdns_thread.start()
    try:
        while True:
            pass
    except KeyboardInterrupt:
        print("Server stopped.")

if __name__ == "__main__":
    main()
