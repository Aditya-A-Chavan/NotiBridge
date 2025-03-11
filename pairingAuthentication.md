# 🔹 Secure Pairing, Authentication, and Dynamic IP Resolution for NotiBridge

This flow ensures **secure pairing, authentication, and automatic reconnection** between the Android and desktop apps over a local Wi-Fi network. It uses **mDNS-based discovery** for dynamic IP resolution and caches the resolved IP for efficient communication.

---

## 🛠 System Overview

### ✅ Key Processes
1. **Pairing Process** (One-time setup via QR Code)
2. **Secure Authentication & Communication**
3. **Unpairing Process** (When user decides to disconnect)
4. **Dynamic IP Resolution using mDNS**
5. **Authentication & Automatic Reconnection**

---

## 1️⃣ Pairing Process (One-time Setup)

The pairing process ensures that only the intended phone and desktop can communicate.

### 📌 Steps:

### 🔹 **Desktop App Generates QR Code**
- Generate a unique **pairing_key** (random 256-bit key).
- Store a secure **device_id** (random UUID).
- Start an **mDNS service** advertising the hostname:
  ```
  _notibridge._tcp._local._<deviceId>
  ```
- Generate a QR code containing:
  ```json
  {
    "device_id": "a1b2c3d4-e567-890f-gh12-3456789ijkl",
    "pairing_key": "e7f1d6c4b29a12345678f90abcdef123",
    "hostname": "_notibridge._tcp._local._<deviceId>"
  }
  ```
- Display the QR Code on the desktop screen.

### 🔹 **Android App Scans QR Code**
- Extracts **device_id**, **pairing_key**, and **hostname**.
- Performs an **mDNS query** to resolve the desktop’s IP and port.
- Generates a unique **phone_id**.
- Sends a **pairing request** to the desktop:
  ```json
  {
    "request": "PAIR",
    "device_id": "a1b2c3d4-e567-890f-gh12-3456789ijkl",
    "phone_id": "abcd-1234-efgh-5678",
    "pairing_key": "e7f1d6c4b29a12345678f90abcdef123"
  }
  ```

### 🔹 **Desktop Verifies Pairing**
- Checks if **pairing_key** is correct.
- Saves **phone_id** & marks it as "PAIRED".
- Sends a success response:
  ```json
  {
    "status": "SUCCESS",
    "message": "Device paired successfully"
  }
  ```
- Stores the **phone_id** permanently.

✅ **Now the devices are securely paired!**
✅ **From now on, authentication is done using phone_id (not pairing_key).**

---

## 2️⃣ Secure Authentication & Communication

### 📌 Steps:
1. **Android app listens** for notifications.
2. Whenever a notification arrives:
   - Uses the **cached IP** (resolved during pairing) to send the notification.
   - If the **cached IP fails**, performs **mDNS resolution again**.
   - Sends the notification to the resolved IP:
     ```json
     {
       "request": "NOTIFICATION",
       "phone_id": "abcd-1234-efgh-5678",
       "notification_data": {
         "title": "New Message",
         "body": "Hey! How are you?",
         "app": "WhatsApp"
       }
     }
     ```
3. **Desktop verifies phone_id** before accepting data.
4. **Notification is displayed** on the desktop.

✅ **Only paired devices can send notifications**
✅ **No hardcoded IPs—always resolved dynamically using mDNS**

---

## 3️⃣ Unpairing Process

Ensures that only the correct phone can unpair the system.

### 📌 Steps:
1. **User requests unpairing** from the Android app.
2. **Android app resolves** the desktop’s IP via **mDNS**.
3. **Android app sends an unpair request** to the desktop:
   ```json
   {
     "request": "UNPAIR",
     "phone_id": "abcd-1234-efgh-5678"
   }
   ```
4. **Desktop checks if the phone_id is correct**.
5. If valid, the desktop:
   - Deletes stored **phone_id**.
   - Sends success response:
     ```json
     {
       "status": "SUCCESS",
       "message": "Unpaired successfully"
     }
     ```
6. **Android app removes stored pairing data**.

✅ **Now the devices are completely disconnected.**
✅ **To pair again, the user must scan a new QR code.**

---

## 4️⃣ Authentication & Automatic Reconnection

Ensures that **already paired devices** can automatically reconnect when on the same network, without requiring a new QR code scan.

### 📌 Steps:

### 🔹 **Android App Behavior**
- Checks if the device is **already paired**.
- Uses the **cached IP** for communication.
- If the cached IP fails, performs **mDNS resolution**.
- Sends an authentication request:
  ```json
  {
    "request": "AUTHENTICATE",
    "phone_id": "abcd-1234-efgh-5678",
    "device_id": "a1b2c3d4-e567-890f-gh12-3456789ijkl"
  }
  ```

### 🔹 **Desktop App Behavior**
- Listens for incoming connections on the **mDNS service**.
- When an authentication request is received:
  - **Verifies phone_id and device_id**.
  - If valid, sends:
    ```json
    {
      "status": "SUCCESS",
      "message": "Authentication successful"
    }
    ```

✅ **Devices automatically reconnect on the same network!**

---

## 5️⃣ Dynamic IP Resolution using mDNS

### 📌 Steps:
1. **During Pairing:**
   - The Android app **resolves the desktop’s IP** using **mDNS** and caches it.
2. **During Communication:**
   - The Android app **uses the cached IP** for sending notifications.
   - If the cached IP **fails**, it **re-resolves the desktop’s IP** using **mDNS**.
3. **During Reconnection:**
   - The Android app **performs mDNS resolution** to find the desktop’s new IP (if it has changed).

### ✅ Benefits of This Approach
- **Efficiency**: Avoids unnecessary mDNS resolution for every message.
- **Reliability**: Automatically re-resolves the IP if the connection fails.
- **Security**: Only paired devices can communicate.
- **Scalability**: Works seamlessly even if the desktop’s IP changes.

---
