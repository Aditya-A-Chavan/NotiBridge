import React from 'react';
import { FaDesktop, FaMobile, FaWifi, FaQrcode, FaLock, FaBell } from 'react-icons/fa';
import { IoMdArrowForward, IoMdRefresh } from 'react-icons/io';

const TechnicalFlowchart = () => {
  return (
    <div className="technical-flowchart">
      <h3 className="flowchart-title">NotiBridge Technical Workflow</h3>
      
      <div className="flowchart-container">
        {/* Pairing Process */}
        <div className="flowchart-section">
          <h4 className="section-title">Pairing Process (One-time Setup)</h4>
          <div className="flow-steps">
            <div className="flow-step">
              <div className="step-icon"><FaDesktop /></div>
              <div className="step-content">
                <h5>Desktop App</h5>
                <ul>
                  <li>Generates unique pairing_key</li>
                  <li>Starts mDNS service</li>
                  <li>Creates QR code with pairing data</li>
                </ul>
              </div>
            </div>
            
            <div className="flow-arrow"><IoMdArrowForward /></div>
            
            <div className="flow-step">
              <div className="step-icon"><FaQrcode /></div>
              <div className="step-content">
                <h5>QR Code Scanning</h5>
                <ul>
                  <li>Android scans QR code</li>
                  <li>Extracts pairing data</li>
                  <li>Resolves desktop IP via mDNS</li>
                </ul>
              </div>
            </div>
            
            <div className="flow-arrow"><IoMdArrowForward /></div>
            
            <div className="flow-step">
              <div className="step-icon"><FaLock /></div>
              <div className="step-content">
                <h5>Authentication</h5>
                <ul>
                  <li>Sends pairing request</li>
                  <li>Desktop verifies pairing_key</li>
                  <li>Devices are now securely paired</li>
                </ul>
              </div>
            </div>
          </div>
        </div>
        
        {/* Communication Workflow */}
        <div className="flowchart-section">
          <h4 className="section-title">Communication Workflow</h4>
          <div className="flow-double-track">
            <div className="track android-track">
              <div className="track-title">
                <FaMobile /> Android App
              </div>
              <div className="track-steps">
                <div className="track-step">
                  <div className="step-number">1</div>
                  <div className="step-detail">NotificationListener captures notifications</div>
                </div>
                <div className="track-step">
                  <div className="step-number">2</div>
                  <div className="step-detail">NetworkManager establishes WebSocket connection</div>
                </div>
                <div className="track-step">
                  <div className="step-number">3</div>
                  <div className="step-detail">Sends notification with phone_id authentication</div>
                </div>
                <div className="track-step">
                  <div className="step-number">4</div>
                  <div className="step-detail">Monitors connection & network changes</div>
                </div>
                <div className="track-step">
                  <div className="step-number">5</div>
                  <div className="step-detail">Uses mDNS to reconnect if connection lost</div>
                </div>
              </div>
            </div>
            
            <div className="track desktop-track">
              <div className="track-title">
                <FaDesktop /> Desktop App
              </div>
              <div className="track-steps">
                <div className="track-step">
                  <div className="step-number">1</div>
                  <div className="step-detail">WebSocketServer listens for connections</div>
                </div>
                <div className="track-step">
                  <div className="step-number">2</div>
                  <div className="step-detail">AuthManager verifies phone_id is paired</div>
                </div>
                <div className="track-step">
                  <div className="step-number">3</div>
                  <div className="step-detail">Accepts & processes authenticated notifications</div>
                </div>
                <div className="track-step">
                  <div className="step-number">4</div>
                  <div className="step-detail">Displays notifications as Windows toasts</div>
                </div>
                <div className="track-step">
                  <div className="step-number">5</div>
                  <div className="step-detail">Broadcasts mDNS service if connection lost</div>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        {/* Dynamic IP Resolution */}
        <div className="flowchart-section">
          <h4 className="section-title">Dynamic IP Resolution & Reconnection</h4>
          <div className="flow-circular">
            <div className="circular-step">
              <div className="step-icon"><FaWifi /></div>
              <div className="step-content">Detect Wi-Fi Network Change</div>
            </div>
            
            <div className="flow-arrow circular-arrow"><IoMdArrowForward /></div>
            
            <div className="circular-step">
              <div className="step-icon"><IoMdRefresh /></div>
              <div className="step-content">Start mDNS Discovery Process</div>
            </div>
            
            <div className="flow-arrow circular-arrow"><IoMdArrowForward /></div>
            
            <div className="circular-step">
              <div className="step-icon"><FaLock /></div>
              <div className="step-content">Re-Authenticate with phone_id</div>
            </div>
            
            <div className="flow-arrow circular-arrow"><IoMdArrowForward /></div>
            
            <div className="circular-step">
              <div className="step-icon"><FaBell /></div>
              <div className="step-content">Resume Notification Transmission</div>
            </div>
            
            <div className="flow-arrow circular-arrow"><IoMdArrowForward /></div>
          </div>
        </div>
      </div>
      
      <div className="technical-notes">
        <h4>Technical Implementation Notes</h4>
        <ul>
          <li><strong>Authentication:</strong> Uses secure token-based authentication with device_id and phone_id</li>
          <li><strong>Service Discovery:</strong> jmDNS on desktop, NetworkServiceDiscovery on Android</li>
          <li><strong>Communication:</strong> Persistent WebSocket for real-time notification delivery</li>
          <li><strong>Pairing:</strong> QR code with ZXing for secure key exchange</li>
          <li><strong>Notifications:</strong> NotificationListenerService on Android, Windows Toast API on desktop</li>
        </ul>
      </div>
    </div>
  );
};

export default TechnicalFlowchart; 