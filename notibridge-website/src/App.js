import React, { useState, useEffect } from 'react';
import './App.css';
import './styles/TechnicalFlowchart.css';
import { FaGithub, FaCode, FaMobile, FaDesktop, FaServer, FaCloud, FaPlus, FaMoon, FaSun, FaBars, FaEnvelope, FaLinkedin, FaTimes, FaWifi, FaQrcode, FaSync, FaFilter, FaSearch, FaLock, FaAndroid, FaWindows, FaDownload } from 'react-icons/fa';
import { BiCodeBlock } from 'react-icons/bi';
import { RiNotification4Line } from 'react-icons/ri';
import { IoMdArrowForward } from 'react-icons/io';
import TechnicalFlowchart from './components/TechnicalFlowchart';

function App() {
  const [darkMode, setDarkMode] = useState(true);
  const [menuOpen, setMenuOpen] = useState(false);
  const [activeFaq, setActiveFaq] = useState(null);

  useEffect(() => {
    if (darkMode) {
      document.body.classList.add('dark-mode');
      document.body.classList.remove('light-mode');
    } else {
      document.body.classList.add('light-mode');
      document.body.classList.remove('dark-mode');
    }
  }, [darkMode]);

  const toggleDarkMode = () => {
    setDarkMode(!darkMode);
  };

  const toggleFaq = (index) => {
    if (activeFaq === index) {
      setActiveFaq(null);
    } else {
      setActiveFaq(index);
    }
  };

  const toggleMenu = () => {
    setMenuOpen(!menuOpen);
  };

  return (
    <div className={darkMode ? 'dark-mode' : 'light-mode'}>
      <header className="header">
        <div className="container">
          <div className="nav-wrapper">
            <button className="mobile-menu-toggle" onClick={toggleMenu}>
              <div className={`hamburger ${menuOpen ? 'open' : ''}`}>
                <span></span>
                <span></span>
                <span></span>
              </div>
            </button>
            
            <div className="logo">NotiBridge</div>
            
            <nav className="navigation">
              <ul className={menuOpen ? 'open' : ''}>
                <li><a href="#features">Features</a></li>
                <li><a href="#tech-stack">Tech Stack</a></li>
                <li><a href="#technical-flow">Technical Flow</a></li>
                <li><a href="#setup-guide">Setup Guide</a></li>
                <li><a href="#about">About</a></li>
                <li><a href="#faq">FAQ</a></li>
              </ul>
            </nav>
            
            <button className="theme-toggle" onClick={toggleDarkMode}>
              {darkMode ? <FaSun /> : <FaMoon />}
            </button>
          </div>
        </div>
      </header>

      <section className="hero">
        <div className="container">
          <div className="hero-content">
            <h1>NotiBridge</h1>
            <div className="subtitle">Connect Your Desktop and Mobile Notifications</div>
            <p className="description">
              I built NotiBridge to solve a personal frustration: missing important notifications while working on my PC. 
              This open-source project runs locally on your Wi-Fi network, keeping your data private while seamlessly 
              bridging notifications between your Android phone and Windows PC.
            </p>

            <div className="key-features-highlight">
              <div className="key-feature">
                <div className="key-feature-icon"><RiNotification4Line /></div>
                <div className="key-feature-text">No external server is ever pinged</div>
              </div>
              <div className="key-feature">
                <div className="key-feature-icon"><FaWifi /></div>
                <div className="key-feature-text">Works over local Wi-Fi</div>
              </div>
              <div className="key-feature">
                <div className="key-feature-icon"><FaQrcode /></div>
                <div className="key-feature-text">Secure and Easy QR Pairing</div>
              </div>
              <div className="key-feature">
                <div className="key-feature-icon"><FaSync /></div>
                <div className="key-feature-text">Auto-Reconnect on network change or device startup</div>
              </div>
            </div>
            
            <div className="development-notice">
              <p>Sorry, I'm still making it! Target to complete: <span className="highlight-date">May 2025 </span>Hover over the buttons to see progress.</p>
            </div>
            
            <div className="buttons-row">
              <div className="button-container github-container">
                <a href="https://github.com/Aditya-A-Chavan/notibridge" className="github-link" target="_blank" rel="noopener noreferrer">
                  <FaGithub className="github-icon" />
                  View on GitHub
                </a>
              </div>
              
              <div className="button-container desktop-container">
                <button className="download-button desktop-app">
                  <FaWindows className="download-icon" />
                  Desktop App
                </button>
                <div className="progress-tooltip" style={{position: 'absolute', top: 'calc(100% + 15px)', left: '50%', transform: 'translateX(-50%)'}}>
                  <div className="tooltip-header">
                    <FaWindows /> Windows App Status
                  </div>
                  <div className="progress-items">
                    <div className="progress-item">
                      <div className="progress-label">Core Workflows</div>
                      <div className="progress-bar">
                        <div className="progress-fill" style={{width: "85%"}}></div>
                      </div>
                      <div className="progress-status done">Ready</div>
                    </div>
                    <div className="progress-item">
                      <div className="progress-label">WebSocket Server</div>
                      <div className="progress-bar">
                        <div className="progress-fill" style={{width: "70%"}}></div>
                      </div>
                      <div className="progress-status in-progress">In Progress</div>
                    </div>
                    <div className="progress-item">
                      <div className="progress-label">UI Development</div>
                      <div className="progress-bar">
                        <div className="progress-fill" style={{width: "40%"}}></div>
                      </div>
                      <div className="progress-status in-progress">In Progress</div>
                    </div>
                    <div className="progress-item">
                      <div className="progress-label">Final Testing</div>
                      <div className="progress-bar">
                        <div className="progress-fill" style={{width: "20%"}}></div>
                      </div>
                      <div className="progress-status pending">Pending</div>
                    </div>
                  </div>
                  <div className="tooltip-footer">Overall: Under Development</div>
                </div>
              </div>
              
              <div className="button-container android-container">
                <button className="download-button android-app">
                  <FaAndroid className="download-icon" />
                  Android App
                </button>
                <div className="progress-tooltip" style={{position: 'absolute', top: 'calc(100% + 15px)', left: '50%', transform: 'translateX(-50%)'}}>
                  <div className="tooltip-header">
                    <FaAndroid /> Android App Status
                  </div>
                  <div className="progress-items">
                    <div className="progress-item">
                      <div className="progress-label">Notification Listener</div>
                      <div className="progress-bar">
                        <div className="progress-fill" style={{width: "100%"}}></div>
                      </div>
                      <div className="progress-status done">Complete</div>
                    </div>
                    <div className="progress-item">
                      <div className="progress-label">mDNS Discovery</div>
                      <div className="progress-bar">
                        <div className="progress-fill" style={{width: "100%"}}></div>
                      </div>
                      <div className="progress-status done">Complete</div>
                    </div>
                    <div className="progress-item">
                      <div className="progress-label">Auto Reconnect</div>
                      <div className="progress-bar">
                        <div className="progress-fill" style={{width: "90%"}}></div>
                      </div>
                      <div className="progress-status done">Working</div>
                    </div>
                    <div className="progress-item">
                      <div className="progress-label">UI Implementation</div>
                      <div className="progress-bar">
                        <div className="progress-fill" style={{width: "30%"}}></div>
                      </div>
                      <div className="progress-status pending">Pending</div>
                    </div>
                  </div>
                  <div className="tooltip-footer">Overall: Basic App Ready</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section id="features">
        <div className="container">
          <div className="section-header">
            <h2>Key Features</h2>
            <p className="section-subtitle">
              Designed to provide a seamless notification experience across all your devices
            </p>
          </div>

          <div className="feature-cards">
            <div className="feature-card">
              <FaDesktop className="feature-icon" />
              <h3>Notification Mirroring</h3>
              <p>
                Real-time sync of Android notifications to Windows.
                Never miss an important message or alert when you step away from your desk.
              </p>
            </div>

            <div className="feature-card">
              <FaMobile className="feature-icon" />
              <h3>Offline Functionality</h3>
              <p>
                Operates over local Wi-Fi with no cloud or internet required.
                Your data stays on your network for maximum privacy.
              </p>
            </div>

            <div className="feature-card">
              <BiCodeBlock className="feature-icon" />
              <h3>QR-Based Secure Pairing</h3>
              <p>
                Fast and secure pairing using QR codes with embedded authentication tokens.
                Simple setup with strong security built-in.
              </p>
            </div>

            <div className="feature-card">
              <FaCode className="feature-icon" />
              <h3>mDNS Auto-Discovery</h3>
              <p>
                Automatically finds devices on your network, even if IPs change.
                Powered by jmDNS on desktop and NetworkServiceDiscovery on Android.
              </p>
            </div>

            <div className="feature-card">
              <RiNotification4Line className="feature-icon" />
              <h3>Persistent Connection</h3>
              <p>
                Maintains a live WebSocket connection for instant notification delivery
                with intelligent auto-reconnect when network changes are detected.
              </p>
            </div>

            <div className="feature-card">
              <FaCloud className="feature-icon" />
              <h3>Modular Design</h3>
              <p>
                Clean architecture with future extensibility in mind. Run as a proper Windows app
                with proper desktop packaging using Launch4j/jPackage.
              </p>
            </div>
          </div>
        </div>
      </section>

      <section id="tech-stack" className="tech-stack">
        <div className="container">
          <div className="section-header">
            <h2>Technology Stack</h2>
            <p className="section-subtitle">
              Built with modern, robust technologies to ensure reliability and performance
            </p>
          </div>

          <div className="tech-stack-container">
            <div className="tech-platform">
              <div className="platform-title">Android App</div>
              <div className="tech-list">
                <div className="tech-item">
                  <div className="tech-name">Kotlin</div>
                  <p className="tech-description">
                    Modern Android development language with strong typing and concise syntax.
                  </p>
                </div>
                <div className="tech-item">
                  <div className="tech-name">Jetpack Compose</div>
                  <p className="tech-description">
                    Modern UI toolkit for building native user interfaces with a declarative approach.
                  </p>
                </div>
                <div className="tech-item">
                  <div className="tech-name">WebSockets & mDNS</div>
                  <p className="tech-description">
                    Real-time communication using WebSockets with OkHttp/Ktor and device discovery via Android's NetworkServiceDiscovery APIs.
                  </p>
                </div>
                <div className="tech-item">
                  <div className="tech-name">NotificationListenerService</div>
                  <p className="tech-description">
                    Android service for capturing system notifications and custom NetworkManager for connection management.
                  </p>
                </div>
              </div>
            </div>

            <div className="tech-platform">
              <div className="platform-title">Desktop Backend (Windows)</div>
              <div className="tech-list">
                <div className="tech-item">
                  <div className="tech-name">Java</div>
                  <p className="tech-description">
                    Robust language powering the Windows desktop application with cross-platform compatibility.
                  </p>
                </div>
                <div className="tech-item">
                  <div className="tech-name">WebSocket Server & jmDNS</div>
                  <p className="tech-description">
                    Jetty/Undertow for WebSocket server and jmDNS for multicast DNS service discovery.
                  </p>
                </div>
                <div className="tech-item">
                  <div className="tech-name">ZXing</div>
                  <p className="tech-description">
                    QR code generation library for secure pairing between devices.
                  </p>
                </div>
                <div className="tech-item">
                  <div className="tech-name">Launch4j/jPackage</div>
                  <p className="tech-description">
                    Packaging tools for creating native Windows executables from Java applications.
                  </p>
                </div>
              </div>
            </div>
          </div>

          <div id="networking" className="system-architecture">
            <div className="architecture-title">Networking Architecture</div>
            <div className="architecture-diagram">
              <div className="component">
                <h4>Device Discovery</h4>
                <ul>
                  <li>mDNS (jmDNS on desktop, NSD on Android)</li>
                  <li>Automatic device detection</li>
                  <li>Works on local networks</li>
                  <li>No cloud services needed</li>
                </ul>
              </div>
              
              <div className="component-arrow">
                <IoMdArrowForward />
              </div>
              
              <div className="component">
                <h4>Connection & Auth</h4>
                <ul>
                  <li>QR-Based Secure Pairing (ZXing)</li>
                  <li>WebSocket Persistent Connection</li>
                  <li>Token-based Authentication</li>
                  <li>Auto-Reconnect Logic</li>
                </ul>
              </div>
              
              <div className="component-arrow">
                <IoMdArrowForward />
              </div>
              
              <div className="component">
                <h4>Data Exchange</h4>
                <ul>
                  <li>WebSocket Real-time Notifications</li>
                  <li>Token Validation</li>
                  <li>Notification Filtering</li>
                  <li>Secure Communication</li>
                </ul>
              </div>
            </div>
          </div>

          <div id="technical-workflow" className="system-architecture">
            <div className="architecture-title">Technical System Workflow</div>
            <TechnicalFlowchart />
          </div>
        </div>
      </section>

      <section id="technical-flow" className="technical-flow">
        <div className="container">
          <div className="section-header">
            <h2>Technical Architecture</h2>
            <p className="section-subtitle">
              A detailed look at how NotiBridge works under the hood
            </p>
          </div>
          
          <div className="flow-container">
            <div className="flow-header">
              <div className="flow-title">End-to-End Notification Flow</div>
            </div>
            <div className="flow-diagram">
              <div className="flow-step">
                <div className="step-number">1</div>
                <div className="step-content">
                  <h4>Notification Generated</h4>
                  <div className="step-icon"><FaMobile /></div>
                  <p>Android notification is captured by NotificationListenerService</p>
                </div>
              </div>
              
              <div className="flow-arrow">→</div>
              
              <div className="flow-step">
                <div className="step-number">2</div>
                <div className="step-content">
                  <h4>Local Processing</h4>
                  <div className="step-icon"><FaFilter /></div>
                  <p>Notification is filtered based on user preferences</p>
                </div>
              </div>
              
              <div className="flow-arrow">→</div>
              
              <div className="flow-step">
                <div className="step-number">3</div>
                <div className="step-content">
                  <h4>Secure Transfer</h4>
                  <div className="step-icon"><FaWifi /></div>
                  <p>Data sent over WebSocket on local Wi-Fi network</p>
                </div>
              </div>
              
              <div className="flow-arrow">→</div>
              
              <div className="flow-step">
                <div className="step-number">4</div>
                <div className="step-content">
                  <h4>Desktop Delivery</h4>
                  <div className="step-icon"><FaDesktop /></div>
                  <p>Windows app receives and displays the notification</p>
                </div>
              </div>
            </div>
            
            <div className="connection-flow">
              <div className="flow-title">Device Discovery & Connection</div>
              <div className="connection-diagram">
                <div className="connection-stage">
                  <h4>Device Discovery</h4>
                  <div className="connection-icon"><FaSearch /></div>
                  <ul className="connection-details">
                    <li>Desktop app broadcasts using jmDNS</li>
                    <li>Android app discovers via NSD APIs</li>
                    <li>Works on local network only</li>
                  </ul>
                </div>
                
                <div className="connection-stage">
                  <h4>Initial Pairing</h4>
                  <div className="connection-icon"><FaQrcode /></div>
                  <ul className="connection-details">
                    <li>Desktop generates QR with ZXing</li>
                    <li>QR contains auth token & connection info</li>
                    <li>Android scans QR code to pair</li>
                  </ul>
                </div>
                
                <div className="connection-stage">
                  <h4>Authentication</h4>
                  <div className="connection-icon"><FaLock /></div>
                  <ul className="connection-details">
                    <li>Token-based authentication</li>
                    <li>Secure handshake over WebSocket</li>
                    <li>Connection established if valid</li>
                  </ul>
                </div>
                
                <div className="connection-stage">
                  <h4>Persistence</h4>
                  <div className="connection-icon"><FaSync /></div>
                  <ul className="connection-details">
                    <li>Auto-reconnect on network changes</li>
                    <li>Auto-discovery if IPs change</li>
                    <li>Persistent local storage of pairing info</li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section id="setup-guide">
        <div className="container">
          <div className="section-header">
            <h2>Setup Guide</h2>
            <p className="section-subtitle">
              Get started with NotiBridge in just a few simple steps
            </p>
          </div>
          
          <div className="setup-steps">
            <div className="setup-step">
              <div className="step-number">1</div>
              <div className="step-content">
                <h3>Install Desktop App</h3>
                <p>Run the installer on your Windows PC and follow the installation wizard. Launch the app once installed.</p>
              </div>
              <div className="step-arrow right"><IoMdArrowForward /></div>
            </div>
            
            <div className="setup-step">
              <div className="step-number">2</div>
              <div className="step-content">
                <h3>Install Android App</h3>
                <p>Install the APK file on your Android device. You may need to allow installation from unknown sources in your settings.</p>
              </div>
              <div className="step-arrow down"><IoMdArrowForward /></div>
            </div>
            
            <div className="setup-step">
              <div className="step-number">3</div>
              <div className="step-content">
                <h3>Generate Pairing Code</h3>
                <p>In the desktop app, click "Generate QR Code" to create a unique pairing code for your devices.</p>
              </div>
              <div className="step-arrow left"><IoMdArrowForward /></div>
            </div>
            
            <div className="setup-step">
              <div className="step-number">4</div>
              <div className="step-content">
                <h3>Scan QR Code</h3>
                <p>Open the Android app and use it to scan the QR code displayed on your desktop. This establishes a secure connection.</p>
              </div>
              <div className="step-arrow down-center"><IoMdArrowForward /></div>
            </div>
            
            <div className="setup-step">
              <div className="step-number">5</div>
              <div className="step-content">
                <h3>Grant Permissions</h3>
                <p>Allow notification access on your Android device when prompted. This lets NotiBridge send your notifications to your PC.</p>
              </div>
            </div>
          </div>
          
          <div className="setup-note">
            <div className="note-icon"><FaLock /></div>
            <p>All communication happens locally over your Wi-Fi network. Your notification data never leaves your network or passes through any external servers.</p>
          </div>
        </div>
      </section>

      <section id="about">
        <div className="container">
          <div className="section-header">
            <h2>About The Developer</h2>
            <p className="section-subtitle">
              A student developer from KJ Somaiya College of Engineering, Mumbai.
            </p>
          </div>

          <div className="developer-profile">
            <div className="developer-info">
              <div className="developer-header">
              <a href="https://github.com/aditya-a-chavan" target="_blank" rel="noopener noreferrer">
                <div className="developer-img-container">
                  
                  <img src="/images/aditya-profile.png" alt="Aditya Chavan" className="developer-img" />
                </div>
                </a>
                <div className="developer-identity">
                  <h3 className="developer-name"> <a href="https://github.com/aditya-a-chavan" target="_blank" rel="noopener noreferrer">Aditya Chavan</a></h3>
                  <div className="developer-title">Software Engineer & Tech Enthusiast</div>
                </div>
              </div>
              <p className="developer-bio">
                I'm a tech enthusiast and aspiring programmer who absolutely loves to know how things work in 
                the backend of the digital world. Currently a student at KJ Somaiya College of Engineering, 
                majoring in Information Technology and seeking opportunities to apply my skills. I'm a nerd when 
                it comes to technology and computers. In my view, hands-on experience trumps classroom learning.
              </p>
              <div className="developer-skills">
                <div className="skills-category">
                  <h4 className="category-title">Languages</h4>
                  <div className="category-skills">
                    <span className="skill-tag java"><i className="devicon-java-plain"></i> JAVA</span>
                    <span className="skill-tag python"><i className="devicon-python-plain"></i> PYTHON</span>
                    <span className="skill-tag javascript"><i className="devicon-javascript-plain"></i> JAVASCRIPT</span>
                    <span className="skill-tag solidity"><i className="devicon-solidity-plain"></i> SOLIDITY</span>
                  </div>
                </div>
                
                <div className="skills-category">
                  <h4 className="category-title">Frameworks & Libraries</h4>
                  <div className="category-skills">
                    <span className="skill-tag spring"><i className="devicon-spring-plain"></i> SPRING</span>
                    <span className="skill-tag nodejs"><i className="devicon-nodejs-plain"></i> NODE.JS</span>
                    <span className="skill-tag express"><i className="devicon-express-original"></i> EXPRESS.JS</span>
                    <span className="skill-tag django"><i className="devicon-django-plain"></i> DJANGO</span>
                    <span className="skill-tag flask"><i className="devicon-flask-original"></i> FLASK</span>
                    <span className="skill-tag socketio"><i className="devicon-socketio-original"></i> SOCKET.IO</span>
                    <span className="skill-tag jwt"><i className="devicon-jwt-plain"></i> JWT</span>
                    <span className="skill-tag vercel"><i className="devicon-vercel-plain"></i> VERCEL</span>
                  </div>
                </div>
                
                <div className="skills-category">
                  <h4 className="category-title">Databases</h4>
                  <div className="category-skills">
                    <span className="skill-tag mongodb"><i className="devicon-mongodb-plain"></i> MONGODB</span>
                    <span className="skill-tag mysql"><i className="devicon-mysql-plain"></i> MYSQL</span>
                    <span className="skill-tag postgres"><i className="devicon-postgresql-plain"></i> POSTGRES</span>
                    <span className="skill-tag redis"><i className="devicon-redis-plain"></i> REDIS</span>
                    <span className="skill-tag neo4j"><i className="devicon-neo4j-plain"></i> NEO4J</span>
                    <span className="skill-tag supabase"><i className="devicon-supabase-plain"></i> SUPABASE</span>
                  </div>
                </div>
                
                <div className="skills-category">
                  <h4 className="category-title">DevOps & Cloud</h4>
                  <div className="category-skills">
                    <span className="skill-tag aws"><i className="devicon-amazonwebservices-original"></i> AWS</span>
                    <span className="skill-tag docker"><i className="devicon-docker-plain"></i> DOCKER</span>
                    <span className="skill-tag kubernetes"><i className="devicon-kubernetes-plain"></i> KUBERNETES</span>
                    <span className="skill-tag firebase"><i className="devicon-firebase-plain"></i> FIREBASE</span>
                    <span className="skill-tag linux"><i className="devicon-linux-plain"></i> LINUX</span>
                  </div>
                </div>
              </div>
              <div className="contact-links">
                <a href="https://github.com/aditya-a-chavan" className="contact-link" target="_blank" rel="noopener noreferrer">
                  <FaGithub /> GitHub
                </a>
                <a href="mailto:adityachavan271@gmail.com" className="contact-link">
                  <FaEnvelope /> Email
                </a>
                <a href="https://www.linkedin.com/in/aditya-chavan-651525272/" className="contact-link" target="_blank" rel="noopener noreferrer">
                  <FaLinkedin /> LinkedIn
                </a>
              </div>
            </div>

          </div>

          <div className="future-plans">
            <h3>Future Development Plans</h3>
            <div className="plans-grid">
              <div className="plan-item">
                <FaPlus className="plan-icon" />
                <div className="plan-title">iOS Support</div>
                <p>Expanding the platform to support iOS devices and notifications.</p>
              </div>
              <div className="plan-item">
                <FaPlus className="plan-icon" />
                <div className="plan-title">macOS Integration</div>
                <p>Adding support for macOS to complete the cross-platform experience.</p>
              </div>
              <div className="plan-item">
                <FaPlus className="plan-icon" />
                <div className="plan-title">Browser Extension</div>
                <p>Creating browser extensions for Chrome and Firefox integration.</p>
              </div>
              <div className="plan-item">
                <FaPlus className="plan-icon" />
                <div className="plan-title">API Enhancements</div>
                <p>Expanding the API for better third-party integrations.</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section id="faq">
        <div className="container">
          <div className="section-header">
            <h2>Frequently Asked Questions</h2>
            <p className="section-subtitle">
              Common questions about the NotiBridge project
            </p>
          </div>

          <div className="faq-container">
            <div className="faq-item">
              <div className="faq-question" onClick={() => toggleFaq(0)}>
                <h3>How does NotiBridge work?</h3>
                <div className={`toggle-icon ${activeFaq === 0 ? 'active' : ''}`}>
                  <FaPlus />
                </div>
              </div>
              <div className={`faq-answer ${activeFaq === 0 ? 'active' : ''}`}>
                <p>
                  NotiBridge establishes a secure WebSocket connection between your devices. When a notification appears on one device, 
                  it's captured, encrypted, and sent to the paired device. The receiving device then displays the notification using 
                  the native notification system.
                </p>
              </div>
            </div>

            <div className="faq-item">
              <div className="faq-question" onClick={() => toggleFaq(1)}>
                <h3>Is my notification data secure?</h3>
                <div className={`toggle-icon ${activeFaq === 1 ? 'active' : ''}`}>
                  <FaPlus />
                </div>
              </div>
              <div className={`faq-answer ${activeFaq === 1 ? 'active' : ''}`}>
                <p>
                  Yes, all notification data is end-to-end encrypted. The data never passes through any third-party servers - 
                  it transfers directly between your devices using a secure WebSocket connection. Your notification content remains 
                  private and secure.
                </p>
              </div>
            </div>

            <div className="faq-item">
              <div className="faq-question" onClick={() => toggleFaq(2)}>
                <h3>How do I contribute to the project?</h3>
                <div className={`toggle-icon ${activeFaq === 2 ? 'active' : ''}`}>
                  <FaPlus />
                </div>
              </div>
              <div className={`faq-answer ${activeFaq === 2 ? 'active' : ''}`}>
                <p>
                  Contributions are welcome! Fork the GitHub repository, make your changes, and submit a pull request. Check out 
                  the CONTRIBUTING.md file in the repository for detailed guidelines. You can also contribute by reporting bugs, 
                  suggesting features, or improving documentation.
                </p>
              </div>
            </div>

            <div className="faq-item">
              <div className="faq-question" onClick={() => toggleFaq(3)}>
                <h3>Which platforms are currently supported?</h3>
                <div className={`toggle-icon ${activeFaq === 3 ? 'active' : ''}`}>
                  <FaPlus />
                </div>
              </div>
              <div className={`faq-answer ${activeFaq === 3 ? 'active' : ''}`}>
                <p>
                  Currently, NotiBridge supports Windows 10/11 for desktop and Android 8.0+ for mobile. We're actively working on 
                  expanding to macOS and iOS in future releases. Check our roadmap on GitHub for more details on upcoming platform support.
                </p>
              </div>
            </div>
          </div>
        </div>
      </section>

      <footer className="footer">
        <div className="container">
          <div className="footer-content">
            <div className="footer-brand">
              <div className="logo">NotiBridge</div>
              <p>
                An open-source project that bridges notifications between your devices. Built with ❤️ by a developer for developers.
              </p>
            </div>
            <div className="footer-links">
              <div className="footer-column">
                <h4>Project</h4>
                <ul>
                  <li><a href="#features">Features</a></li>
                  <li><a href="#tech-stack">Tech Stack</a></li>
                  <li><a href="#about">About</a></li>
                  <li><a href="#faq">FAQ</a></li>
                </ul>
              </div>
              <div className="footer-column">
                <h4>Resources</h4>
                <ul>
                  <li><a href="https://github.com/aditya-a-chavan/notibridge" target="_blank" rel="noopener noreferrer">GitHub</a></li>
                  <li><a href="https://github.com/aditya-a-chavan/notibridge/issues" target="_blank" rel="noopener noreferrer">Issues</a></li>
                  <li><a href="https://github.com/aditya-a-chavan/notibridge/wiki" target="_blank" rel="noopener noreferrer">Documentation</a></li>
                  <li><a href="https://github.com/aditya-a-chavan/notibridge/releases" target="_blank" rel="noopener noreferrer">Releases</a></li>
                </ul>
              </div>
            </div>
          </div>
          <div className="copyright">
            By Aditya Chavan 😎
          </div>
        </div>
      </footer>
    </div>
  );
}

export default App;
