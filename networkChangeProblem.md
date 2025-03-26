# 🔹 Dynamic IP Resolution for NotiBridge

## ❓ Problem: What If the Desktop's IP Changes?
If the **desktop’s IP changes** due to switching Wi-Fi networks or restarting the router, the Android app won’t know where to send notifications.  
We need a way to **dynamically resolve the desktop’s IP**.

---

### ✅ Solution: mDNS (Multicast DNS)
#### 🔹 Working:
- The **desktop advertises its presence** on the local network with a unique hostname structure (e.g., `<DEVICE_ID>._notibridge._tcp.local`).
- The **Android app discovers the desktop** by looking for this hostname.
- No need to manually configure IPs!

#### 🔹 Implementation:
- **Desktop App:** Runs an mDNS responder (e.g., `python-zeroconf` in Python or `dnssd` in Kotlin).
- **Android App:** Queries the network for `<DEVICE_ID>._notibridge._tcp.local` and retrieves the correct IP.

✅ **Automatic discovery even if IP changes**  
✅ **Works completely offline**  
