import React from 'react';
import { FaGithub, FaExternalLinkAlt } from 'react-icons/fa';

const ProjectSpecs = () => {
  return (
    <section id="project-specs" className="project-specs-section">
      <div className="project-specs-container">
        <h2 className="section-title">Project Specifications</h2>
        
        <div className="project-specs-grid">
          <div className="specs-card">
            <h3 className="specs-title">Tech Stack</h3>
            <ul className="specs-list">
              <li>Frontend: React.js with Next.js framework</li>
              <li>Backend: Node.js with Express</li>
              <li>Database: MongoDB for user data and notification storage</li>
              <li>Authentication: JWT with refresh token rotation</li>
              <li>Real-time: WebSockets for instant notifications</li>
              <li>Native Integration: Electron for desktop, React Native for mobile</li>
            </ul>
          </div>
          
          <div className="specs-card">
            <h3 className="specs-title">Architecture</h3>
            <ul className="specs-list">
              <li>Microservices-based architecture with API gateway</li>
              <li>Event-driven notification delivery system</li>
              <li>Webhook support for third-party integrations</li>
              <li>Local database caching for offline capabilities</li>
              <li>End-to-end encryption for sensitive notifications</li>
              <li>Containerized deployment with Docker and Kubernetes</li>
            </ul>
          </div>
          
          <div className="specs-card">
            <h3 className="specs-title">Development Practices</h3>
            <ul className="specs-list">
              <li>Test-Driven Development with Jest and Cypress</li>
              <li>CI/CD pipeline with GitHub Actions</li>
              <li>Code quality enforcement with ESLint and Prettier</li>
              <li>Automated version management with semantic-release</li>
              <li>Documentation with Storybook and TypeDoc</li>
              <li>Accessibility compliance with WCAG 2.1 standards</li>
            </ul>
          </div>
          
          <div className="specs-card">
            <h3 className="specs-title">Performance Metrics</h3>
            <ul className="specs-list">
              <li>Lighthouse score: 96+ on all categories</li>
              <li>First meaningful paint: < 1.2s</li>
              <li>Time to interactive: < 2.5s</li>
              <li>Notification delivery latency: < 500ms</li>
              <li>Offline functionality: 90% feature coverage</li>
              <li>Bundle size: < 250KB (gzipped)</li>
            </ul>
          </div>
        </div>
        
        <div className="project-links">
          <a href="https://github.com/username/notibridge" className="project-link github" target="_blank" rel="noopener noreferrer">
            <FaGithub /> View Source
          </a>
          <a href="https://docs.notibridge.dev" className="project-link docs" target="_blank" rel="noopener noreferrer">
            <FaExternalLinkAlt /> Documentation
          </a>
          <a href="https://demo.notibridge.dev" className="project-link demo" target="_blank" rel="noopener noreferrer">
            <FaExternalLinkAlt /> Live Demo
          </a>
        </div>
      </div>
    </section>
  );
};

export default ProjectSpecs; 