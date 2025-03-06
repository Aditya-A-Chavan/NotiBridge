# NotiBridge: Secure Desktop-Android Notification Sync System

## TechStack:
1. **Android app (client)**
    - Kotlin, TCP Sockets, ZXing (QR Code), NotificationListenerService

2. **Desktop**
    - Python, socket (TCP Server), qrcode, plyer/win10toast

## Phase 1: QR Code-Based Pairing & Authentication
**Goal**: Secure and seamless pairing before notification transmission.

1. **Desktop Generates a Pairing Code**
   - Start a local TCP server and generate a unique pairing token
   - Encode this pairing data as a QR Code

2. **Android App Scans the QR Code**
   - Use Android's Camera API to scan the QR Code
   - Extract IP Address & Authentication Key
   - Store pairing data for future use

3. **Establish Secure Connection with Authentication**
   - Android sends the authentication key to the desktop
   - Desktop validates it before accepting notifications

**Checkpoint**: Only authorized devices can send notifications

## Phase 2: Notification Capture & Transmission
**Goal:** Capture phone notifications and send them to the desktop.

4. **Android Notification Listener Service**
   - Request **Notification Access Permission**
   - Listen for **incoming notifications** and extract:
      * App Name
      * Title
      * Content
      * Timestamp

5. **Send Notifications via TCP**
   - Modify the **Android TCP client** to send notifications instead of test messages

**Checkpoint:** By the end of this phase, your desktop should **receive real notifications** sent from your Android app.

## Phase 3: Notification Display on Desktop
**Goal:** Show notifications on the laptop when received.

6. **Desktop Notification Handling**
   - Parse the received notification data
   - Display a notification using:
      * `plyer.notification` (Python, cross-platform)
      * `win10toast` (Windows-specific)
      * **Or use a GUI-based approach like Electron (optional)**

**Checkpoint:** At this stage, notifications should pop up on your laptop when received from your phone.

## Phase 4: Auto-Reconnect & Auto-Start

7. **Store Authentication & Auto-Reconnect**
   - Save the pairing data and auto-reconnect when both devices are on the same Wi-Fi

8. **Run the Desktop App in Background**
   - Auto-start the TCP server on system boot

9. **Run Android App in Background**
   - Keep the service running as a Foreground Service to avoid being killed

**Checkpoint**: App works automatically without user intervention