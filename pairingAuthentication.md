# **🔹 Secure Pairing, Authentication, and Unpairing Flow for NotiBridge**  
This flow ensures **offline communication**, **secure pairing**, and **authentication** between the Android app and the desktop app over a **local Wi-Fi network**.

---

# **🛠 System Overview**
1. **Pairing Process** (Initial setup via QR Code)
2. **Authentication & Secure Communication**
3. **Unpairing Process** (When user decides to disconnect)

---

# **1️⃣ Pairing Process (One-time setup)**
The pairing process ensures that only the intended **phone and desktop** can communicate.  

### **📌 Steps:**
### **🔹 Desktop App Generates QR Code**
1. **Generate a unique `pairing_key`** (random 256-bit key).  
2. **Store a secure `device_id`** (random UUID).  
3. **Start a TCP listener on `IP:PORT`** (e.g., `192.168.1.101:5000`).  
4. **Generate a QR code** containing:
   ```json
   {
     "device_id": "a1b2c3d4-e567-890f-gh12-3456789ijkl",
     "ip": "192.168.1.101",
     "port": 5000,
     "pairing_key": "e7f1d6c4b29a12345678f90abcdef123"
   }
   ```
5. **Display the QR Code** on the desktop screen.

---

### **🔹 Android App Scans QR Code**
1. Extracts `device_id`, `IP`, `port`, and `pairing_key`.
2. Generates a **unique phone identifier** (`phone_id`).
3. Sends a **pairing request** to the desktop:
   ```json
   {
     "request": "PAIR",
     "device_id": "a1b2c3d4-e567-890f-gh12-3456789ijkl",
     "phone_id": "abcd-1234-efgh-5678",
     "pairing_key": "e7f1d6c4b29a12345678f90abcdef123"
   }
   ```

---

### **🔹 Desktop Verifies Pairing**
1. **Checks if `pairing_key` is correct**.
2. **Saves `phone_id` & marks it as "PAIRED"**.
3. Sends a **success response**:
   ```json
   {
     "status": "SUCCESS",
     "message": "Device paired successfully"
   }
   ```
4. **Stores the `phone_id` permanently**.

✅ **Now the devices are securely paired!**  
✅ **From now on, authentication is done using `phone_id`** (not `pairing_key`).

---

# **2️⃣ Secure Authentication & Communication**
### **📌 Steps:**
1. **Android app listens for notifications.**
2. **Whenever a notification arrives**, it sends:
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
3. **Desktop verifies `phone_id`** before accepting data.
4. **Notification is displayed on the desktop.**

✅ **Only paired devices can send notifications**  
✅ **Even if someone on the network knows the IP, they can't send fake notifications**  

---

# **3️⃣ Unpairing Process**
The unpairing process ensures that **only the correct phone can unpair** the system.

### **📌 Steps:**
1. **User requests unpairing from the Android app.**
2. **Android app sends an unpair request** to the desktop:
   ```json
   {
     "request": "UNPAIR",
     "phone_id": "abcd-1234-efgh-5678"
   }
   ```
3. **Desktop checks if the `phone_id` is correct**.
4. If valid, the desktop:
   - **Deletes stored `phone_id`**.
   - **Sends success response**:
     ```json
     {
       "status": "SUCCESS",
       "message": "Unpaired successfully"
     }
     ```
5. **Android app removes stored pairing data.**

✅ **Now the devices are completely disconnected.**  
✅ **To pair again, the user must scan a new QR code.**  

---

# **🔹 Security Features in This Flow**
✅ **No Hardcoded IP/Port** → Dynamically obtained via QR Code  
✅ **Pairing Key Used Only Once** → Prevents unauthorized pairing  
✅ **Authentication via `phone_id`** → Only paired devices can send data  
✅ **Unpairing Requires Authentication** → Prevents external attacks  
✅ **Encrypted Communication** → Upgrade to **AES or TLS** for full security  

---
