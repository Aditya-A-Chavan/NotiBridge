# **🔹 Secure Pairing, Authentication, and Dynamic IP Resolution for NotiBridge**  

This updated flow integrates **mDNS-based discovery** for automatic IP resolution, ensuring **offline communication, secure pairing, and authentication** between the Android and desktop apps over a **local Wi-Fi network**.

---

## **🛠 System Overview**
1. **Pairing Process** (One-time setup via QR Code)  
2. **Authentication & Secure Communication**  
3. **Unpairing Process** (When user decides to disconnect)  
4. **Dynamic IP Resolution using mDNS**  

---

## **1️⃣ Pairing Process (One-time setup)**
The pairing process ensures that only the intended **phone and desktop** can communicate.

### **📌 Steps:**
### **🔹 Desktop App Generates QR Code**
1. **Generate a unique `pairing_key`** (random 256-bit key).  
2. **Store a secure `device_id`** (random UUID).  
3. **Start an mDNS service** advertising the hostname:  
   ```
   _notibridge._tcp.local -> <device_id>
   ```
4. **Generate a QR code** containing:
   ```json
   {
     "device_id": "a1b2c3d4-e567-890f-gh12-3456789ijkl",
     "pairing_key": "e7f1d6c4b29a12345678f90abcdef123",
     "hostname": "_notibridge._tcp.local"
   }
   ```
5. **Display the QR Code** on the desktop screen.

---

### **🔹 Android App Scans QR Code**
1. Extracts `device_id`, `pairing_key`, and `hostname`.  
2. Performs an **mDNS query** to resolve the desktop’s IP and port.  
3. Generates a **unique `phone_id`**.  
4. Sends a **pairing request** to the desktop:
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

## **2️⃣ Secure Authentication & Communication**
### **📌 Steps:**
1. **Android app listens for notifications.**  
2. **Whenever a notification arrives**, it:  
   - Resolves the desktop’s **IP dynamically** via **mDNS**.  
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
3. **Desktop verifies `phone_id`** before accepting data.  
4. **Notification is displayed on the desktop.**  

✅ **Only paired devices can send notifications**  
✅ **Even if someone on the network knows the IP, they can't send fake notifications**  
✅ **No hardcoded IPs—always resolved dynamically using mDNS**  

---

## **3️⃣ Unpairing Process**
Ensures that **only the correct phone can unpair** the system.

### **📌 Steps:**
1. **User requests unpairing from the Android app.**  
2. **Android app resolves the desktop’s IP via mDNS.**  
3. **Android app sends an unpair request** to the desktop:
   ```json
   {
     "request": "UNPAIR",
     "phone_id": "abcd-1234-efgh-5678"
   }
   ```
4. **Desktop checks if the `phone_id` is correct**.  
5. If valid, the desktop:  
   - **Deletes stored `phone_id`**.  
   - **Sends success response**:
     ```json
     {
       "status": "SUCCESS",
       "message": "Unpaired successfully"
     }
     ```
6. **Android app removes stored pairing data.**  

✅ **Now the devices are completely disconnected.**  
✅ **To pair again, the user must scan a new QR code.**  
