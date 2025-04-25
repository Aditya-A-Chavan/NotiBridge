import React from 'react';
import { FaGithub } from 'react-icons/fa';

function Footer() {
  return (
    <footer className="footer">
      <div className="container">
        <div className="footer-content">
          <div className="footer-brand">
            <div className="logo">NotiBridge</div>
            <p>
              An open-source project that bridges notifications between your devices. Built with ❤️ by developers for developers.
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
          © {new Date().getFullYear()} NotiBridge. Open source under MIT License.
        </div>
      </div>
    </footer>
  );
}

export default Footer; 