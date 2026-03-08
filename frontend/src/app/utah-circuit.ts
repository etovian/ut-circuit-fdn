import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-utah-circuit',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="circuit-container">
      <nav class="top-nav">
        <div class="nav-content">
          <div class="brand">
            <span class="district-name">Rocky Mountain District</span>
            <span class="circuit-name">Utah Circuit Hub</span>
          </div>
          <div class="nav-links">
            <a href="#congregations">Congregations</a>
            <a href="#resources">Resources</a>
            <a href="#events">Events</a>
            <a routerLink="/" class="home-icon">
              <svg xmlns="http://www.w3.org/2000/svg" height="20" viewBox="0 -960 960 960" width="20" fill="currentColor">
                <path d="M240-200h120v-240h240v240h120v-360L480-740 240-560v360Zm-80 80v-480l320-240 320 240v480H520v-240h-80v240H160Z"/>
              </svg>
            </a>
          </div>
        </div>
      </nav>

      <header class="hub-hero">
        <div class="hero-content">
          <h1>Utah Circuit</h1>
          <p>Connecting LCMS Congregations across the Beehive State</p>
          <div class="search-bar">
            <input type="text" placeholder="Find a congregation...">
            <button>Search</button>
          </div>
        </div>
      </header>

      <main class="hub-main">
        <section id="congregations" class="hub-section">
          <div class="section-title">
            <h2>Our Congregations</h2>
            <p>Faithful communities serving Utah families</p>
          </div>
          <div class="congregation-grid">
            <a routerLink="/cross-of-christ" class="hub-card">
              <div class="card-accent"></div>
              <h3>Cross of Christ</h3>
              <p class="loc">Bountiful, UT</p>
              <span class="view-link">View Page →</span>
            </a>
            <div class="hub-card disabled">
              <div class="card-accent secondary"></div>
              <h3>Redeemer</h3>
              <p class="loc">Salt Lake City, UT</p>
              <span class="view-link">Coming Soon</span>
            </div>
            <div class="hub-card disabled">
              <div class="card-accent secondary"></div>
              <h3>First Lutheran</h3>
              <p class="loc">Tooele, UT</p>
              <span class="view-link">Coming Soon</span>
            </div>
          </div>
        </section>

        <section id="resources" class="resources-grid">
          <div class="resource-item">
            <div class="icon-box">
              <svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24" fill="currentColor">
                <path d="M480-480q33 0 56.5-23.5T560-560q0-33-23.5-56.5T480-640q-33 0-56.5 23.5T400-560q0 33 23.5 56.5T480-480Zm0 240q-83 0-156-31.5T197-357q-54-54-85.5-127T80-640q0-83 31.5-156T197-883q54-54 127-85.5T480-1000q83 0 156 31.5T883-883q54 54 85.5 127T1000-640q0 83-31.5 156T763-357q-54 54-127 85.5T480-240Z"/>
              </svg>
            </div>
            <h4>Circuit Visitor</h4>
            <p>Administrative support and spiritual oversight for the circuit.</p>
          </div>
          <div class="resource-item">
            <div class="icon-box">
              <svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24" fill="currentColor">
                <path d="M200-120q-33 0-56.5-23.5T120-200v-560q0-33 23.5-56.5T200-840h560q33 0 56.5 23.5T840-760v560q0 33-23.5 56.5T760-120H200Zm0-80h560v-560H200v560Zm80-80h400v-80H280v80Zm0-160h400v-80H280v80Zm0-160h400v-80H280v80Z"/>
              </svg>
            </div>
            <h4>Latest News</h4>
            <p>Updates from across the Utah Circuit.</p>
          </div>
          <div class="resource-item">
            <div class="icon-box">
              <svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 -960 960 960" width="24" fill="currentColor">
                <path d="M480-80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Z"/>
              </svg>
            </div>
            <h4>District Forms</h4>
            <p>Access treasury, call, and vacancy documents.</p>
          </div>
        </section>
      </main>

      <footer class="hub-footer">
        <div class="footer-content">
          <p>© 2026 Utah Circuit LCMS - Rocky Mountain District</p>
          <div class="social-links">
            <a href="#">District Site</a>
            <a href="#">LCMS.org</a>
          </div>
        </div>
      </footer>
    </div>
  `,
  styles: [`
    :host {
      --lcms-blue: #003366;
      --lcms-light-blue: #005599;
      --lcms-gold: #c5a059;
      --lcms-cream: #f4f4f4;
      --white: #ffffff;
      --text-dark: #333333;
      --text-light: #666666;

      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      display: block;
      background: var(--lcms-cream);
      color: var(--text-dark);
      min-height: 100vh;
    }

    .top-nav {
      background: var(--lcms-blue);
      color: white;
      padding: 1rem 2rem;
      position: sticky;
      top: 0;
      z-index: 100;
      box-shadow: 0 2px 10px rgba(0,0,0,0.2);
    }

    .nav-content {
      max-width: 1200px;
      margin: 0 auto;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .brand {
      display: flex;
      flex-direction: column;
    }

    .district-name {
      font-size: 0.75rem;
      text-transform: uppercase;
      letter-spacing: 0.1rem;
      opacity: 0.8;
    }

    .circuit-name {
      font-size: 1.25rem;
      font-weight: 700;
      color: var(--lcms-gold);
    }

    .nav-links {
      display: flex;
      gap: 2rem;
      align-items: center;
    }

    .nav-links a {
      color: white;
      text-decoration: none;
      font-weight: 500;
      font-size: 0.9rem;
      transition: color 0.2s;
    }

    .nav-links a:hover {
      color: var(--lcms-gold);
    }

    .hub-hero {
      background: linear-gradient(rgba(0,51,102,0.8), rgba(0,51,102,0.8)),
                  url('https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?auto=format&fit=crop&q=80&w=1600');
      background-size: cover;
      background-position: center;
      color: white;
      padding: 6rem 2rem;
      text-align: center;
    }

    .hero-content h1 {
      font-size: 3.5rem;
      margin: 0;
      letter-spacing: -0.05rem;
    }

    .hero-content p {
      font-size: 1.25rem;
      opacity: 0.9;
      margin: 1rem 0 2.5rem;
    }

    .search-bar {
      display: flex;
      max-width: 500px;
      margin: 0 auto;
      background: white;
      padding: 0.5rem;
      border-radius: 3rem;
      box-shadow: 0 10px 25px rgba(0,0,0,0.2);
    }

    .search-bar input {
      flex: 1;
      border: none;
      padding: 0 1.5rem;
      font-size: 1rem;
      outline: none;
      border-radius: 3rem 0 0 3rem;
    }

    .search-bar button {
      background: var(--lcms-blue);
      color: white;
      border: none;
      padding: 0.75rem 2rem;
      border-radius: 3rem;
      font-weight: 600;
      cursor: pointer;
      transition: background 0.2s;
    }

    .search-bar button:hover {
      background: var(--lcms-light-blue);
    }

    .hub-main {
      max-width: 1200px;
      margin: 0 auto;
      padding: 4rem 2rem;
    }

    .section-title {
      text-align: center;
      margin-bottom: 3.5rem;
    }

    .section-title h2 {
      font-size: 2.25rem;
      color: var(--lcms-blue);
      margin-bottom: 0.5rem;
    }

    .section-title p {
      color: var(--text-light);
    }

    .congregation-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
      gap: 2rem;
    }

    .hub-card {
      background: white;
      padding: 2rem;
      border-radius: 0.75rem;
      text-decoration: none;
      color: inherit;
      box-shadow: 0 4px 15px rgba(0,0,0,0.05);
      transition: transform 0.2s, box-shadow 0.2s;
      position: relative;
      overflow: hidden;
    }

    .hub-card:hover:not(.disabled) {
      transform: translateY(-5px);
      box-shadow: 0 10px 25px rgba(0,0,0,0.1);
    }

    .card-accent {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 4px;
      background: var(--lcms-gold);
    }

    .card-accent.secondary {
      background: var(--gray-400);
    }

    .hub-card h3 {
      font-size: 1.5rem;
      margin-bottom: 0.25rem;
      color: var(--lcms-blue);
    }

    .hub-card.disabled h3 {
      color: var(--gray-400);
    }

    .loc {
      color: var(--text-light);
      font-size: 0.9rem;
      margin-bottom: 1.5rem;
    }

    .view-link {
      font-weight: 600;
      color: var(--lcms-light-blue);
      font-size: 0.875rem;
    }

    .disabled .view-link {
      color: var(--gray-400);
    }

    .resources-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 3rem;
      margin-top: 6rem;
      padding: 4rem;
      background: var(--lcms-blue);
      color: white;
      border-radius: 1rem;
    }

    .resource-item {
      text-align: center;
    }

    .icon-box {
      width: 60px;
      height: 60px;
      background: rgba(255,255,255,0.1);
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin: 0 auto 1.5rem;
      color: var(--lcms-gold);
    }

    .resource-item h4 {
      font-size: 1.25rem;
      margin-bottom: 0.75rem;
    }

    .resource-item p {
      font-size: 0.9rem;
      opacity: 0.8;
      line-height: 1.6;
    }

    .hub-footer {
      background: #1a1a1a;
      color: white;
      padding: 3rem 2rem;
      margin-top: 4rem;
    }

    .footer-content {
      max-width: 1200px;
      margin: 0 auto;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .footer-content p {
      font-size: 0.875rem;
      opacity: 0.6;
    }

    .social-links {
      display: flex;
      gap: 2rem;
    }

    .social-links a {
      color: white;
      text-decoration: none;
      font-size: 0.875rem;
      opacity: 0.8;
      transition: opacity 0.2s;
    }

    .social-links a:hover {
      opacity: 1;
      text-decoration: underline;
    }

    @media (max-width: 768px) {
      .footer-content { flex-direction: column; gap: 1.5rem; text-align: center; }
      .nav-links { display: none; }
      .hero-content h1 { font-size: 2.5rem; }
    }
  `]
})
export class UtahCircuit {}
