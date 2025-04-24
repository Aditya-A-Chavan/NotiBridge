import React from 'react';
import { FaGithub, FaLinkedin, FaTwitter, FaEnvelope } from 'react-icons/fa';
import '../styles/DeveloperProfile.css';

const DeveloperProfile = () => {
  return (
    <section id="developer" className="developer-section">
      <div className="developer-container">
        <h2 className="section-title">About the Developer</h2>
        
        <div className="developer-content">
          <div className="developer-image">
            {/* Replace with your actual profile image */}
            <div className="profile-placeholder">
              <span>Your Photo</span>
            </div>
          </div>
          
          <div className="developer-info">
            <h3 className="developer-name">Your Name</h3>
            <p className="developer-title">Full Stack Developer</p>
            
            <div className="developer-bio">
              <p>
                I'm a passionate full stack developer with expertise in building cross-platform notification systems.
                With over X years of experience in web and mobile development, I specialize in creating
                robust, scalable applications that solve real-world problems.
              </p>
              <p>
                NotiBridge is my solution to the fragmented notification landscape, designed to help
                developers implement unified notification systems seamlessly across multiple platforms.
              </p>
            </div>
            
            <div className="developer-skills">
              <div className="skill-category">
                <h4>Technical Skills</h4>
                <ul className="skills-list">
                  <li>JavaScript/TypeScript</li>
                  <li>React & Next.js</li>
                  <li>Node.js & Express</li>
                  <li>MongoDB & SQL</li>
                  <li>RESTful APIs & GraphQL</li>
                </ul>
              </div>
              
              <div className="skill-category">
                <h4>Specializations</h4>
                <ul className="skills-list">
                  <li>Notification Systems</li>
                  <li>API Integration</li>
                  <li>Cross-Platform Development</li>
                  <li>Microservices Architecture</li>
                  <li>Serverless Computing</li>
                </ul>
              </div>
            </div>
            
            <div className="developer-contact">
              <a href="https://github.com/yourusername" className="social-link github">
                <FaGithub /> GitHub
              </a>
              <a href="https://linkedin.com/in/yourusername" className="social-link linkedin">
                <FaLinkedin /> LinkedIn
              </a>
              <a href="https://twitter.com/yourusername" className="social-link twitter">
                <FaTwitter /> Twitter
              </a>
              <a href="mailto:your.email@example.com" className="social-link email">
                <FaEnvelope /> Email
              </a>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default DeveloperProfile; 