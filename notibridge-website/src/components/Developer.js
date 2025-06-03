import React from 'react';
import { FaGithub, FaLinkedin, FaTwitter, FaEnvelope } from 'react-icons/fa';

const Developer = () => {
  return (
    <section id="developer" className="developer-section">
      <div className="developer-container">
        <div className="developer-header">
          <h2 className="section-title">About the Developer</h2>
        </div>
        
        <div className="developer-profile">
          <img 
            src="/images/developer.jpg" 
            alt="Developer Profile" 
            className="developer-image"
          />
          <h3 className="developer-name">John Doe</h3>
          <p className="developer-title">Full Stack Developer & Notification Systems Architect</p>
          
          <p className="developer-bio">
            I'm a passionate software engineer with over 8 years of experience building robust web applications 
            and distributed systems. NotiBridge is my open-source project aimed at solving cross-platform 
            notification challenges. When I'm not coding, you can find me contributing to open source,
            writing technical articles, and mentoring junior developers.
          </p>
          
          <div className="developer-skills">
            <span className="skill-tag">JavaScript</span>
            <span className="skill-tag">React</span>
            {/* <span className="skill-tag">Node.js</span> */}
            {/* <span className="skill-tag">Express</span> */}
            <span className="skill-tag">MongoDB</span>
            <span className="skill-tag">AWS</span>
            <span className="skill-tag">Docker</span>
            <span className="skill-tag">Microservices</span>
            <span className="skill-tag">API Design</span>
            <span className="skill-tag">CI/CD</span>
          </div>
          
          <div className="social-links">
            <a href="https://github.com/johndoe" className="social-link" target="_blank" rel="noopener noreferrer">
              <FaGithub />
            </a>
            <a href="https://linkedin.com/in/johndoe" className="social-link" target="_blank" rel="noopener noreferrer">
              <FaLinkedin />
            </a>
            <a href="https://twitter.com/johndoe" className="social-link" target="_blank" rel="noopener noreferrer">
              <FaTwitter />
            </a>
            <a href="mailto:john@example.com" className="social-link">
              <FaEnvelope />
            </a>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Developer; 