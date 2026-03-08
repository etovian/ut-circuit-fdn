import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-cross-of-christ',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="church-container">
      <header class="hero">
        <div class="hero-overlay">
          <div class="hero-content">
            <span class="location-tag">Bountiful, Utah</span>
            <h1>Cross of Christ</h1>
            <p class="tagline">"Sharing the love of Christ with Bountiful since 1958"</p>
            <div class="cta-group">
              <a href="#worship" class="pill primary">Worship Times</a>
              <a href="#about" class="pill secondary">Our Mission</a>
            </div>
          </div>
        </div>
      </header>

      <main class="content-wrapper">
        <section id="worship" class="worship-section">
          <div class="section-header">
            <div class="accent-bar"></div>
            <h2>Join Us for Worship</h2>
          </div>
          <div class="worship-grid">
            <div class="worship-card">
              <h3>Sunday Service</h3>
              <p class="time">10:30 AM</p>
              <p class="desc">A traditional Lutheran service with communion and fellowship.</p>
            </div>
            <div class="worship-card">
              <h3>Bible Study</h3>
              <p class="time">9:15 AM</p>
              <p class="desc">Deep dive into scripture and congregational learning.</p>
            </div>
          </div>
        </section>

        <div class="horizontal-divider"></div>

        <section id="about" class="about-section">
          <div class="about-grid">
            <div class="about-text">
              <h2>About Our Congregation</h2>
              <p>Cross of Christ is a community of believers dedicated to the Lutheran heritage and focused on outreach in the Bountiful community. We are a "Mission Station" under the LCMS, committed to the pure teaching of the Gospel.</p>
              <p>Our unique A-frame sanctuary has been a landmark of faith in Bountiful for over 60 years.</p>
            </div>
            <div class="location-info">
              <h3>Find Us</h3>
              <address>
                184 N 200 W<br>
                Bountiful, UT 84010
              </address>
              <a href="https://www.google.com/maps/search/?api=1&query=Cross+of+Christ+Lutheran+Church+Bountiful+UT" target="_blank" class="map-link">
                View on Google Maps
                <svg xmlns="http://www.w3.org/2000/svg" height="18" viewBox="0 -960 960 960" width="18" fill="currentColor">
                  <path d="m600-120-240-84-186 72q-20 8-37-4.5T120-170v-560q0-13 7.5-23t20.5-15l212-72 240 84 186-72q20-8 37 4.5t17 33.5v560q0 13-7.5 23T812-192l-212 72Zm-40-98v-468l-160-56v468l160 56Zm80 0 120-40v-474l-120 46v468Zm-440-10 120-46v-468l-120 40v474Zm440-458v468-468Zm-320-56v468-468Z"/>
                </svg>
              </a>
            </div>
          </div>
        </section>
      </main>

      <footer class="church-footer">
        <a routerLink="/" class="back-link">
          <svg xmlns="http://www.w3.org/2000/svg" height="18" viewBox="0 -960 960 960" width="18" fill="currentColor">
            <path d="m313-440 224 224-57 56-320-320 320-320 57 56-224 224h487v80H313Z"/>
          </svg>
          Back to Foundation
        </a>
      </footer>
    </div>
  `,
  styles: [`
    :host {
      --burgundy: oklch(45% 0.15 20);
      --cream: oklch(98% 0.02 85);
      --gold: oklch(75% 0.15 85);
      --navy: oklch(30% 0.1 260);
      --gray-900: oklch(19.37% 0.006 300.98);
      --gray-700: oklch(36.98% 0.014 302.71);
      --gray-400: oklch(70.9% 0.015 304.04);
      --accent-gradient: linear-gradient(90deg, var(--burgundy) 0%, var(--navy) 100%);
      
      font-family: "Inter", -apple-system, sans-serif;
      display: block;
      background: var(--cream);
      color: var(--gray-900);
      min-height: 100vh;
    }

    .church-container {
      display: flex;
      flex-direction: column;
    }

    .hero {
      height: 60vh;
      min-height: 400px;
      background: url('https://images.unsplash.com/photo-1548625149-fc4a29cf7092?auto=format&fit=crop&q=80&w=1600') center/cover no-repeat;
      position: relative;
    }

    .hero-overlay {
      position: absolute;
      inset: 0;
      background: linear-gradient(180deg, rgba(0,0,0,0.3) 0%, rgba(0,0,0,0.7) 100%);
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 2rem;
      text-align: center;
    }

    .hero-content {
      max-width: 800px;
      color: white;
    }

    .location-tag {
      text-transform: uppercase;
      letter-spacing: 0.2rem;
      font-size: 0.875rem;
      font-weight: 600;
      color: var(--gold);
      margin-bottom: 1rem;
      display: block;
    }

    h1 {
      font-size: 4rem;
      font-weight: 700;
      margin: 0;
      letter-spacing: -0.1rem;
      line-height: 1;
    }

    .tagline {
      font-size: 1.5rem;
      margin-top: 1.5rem;
      font-style: italic;
      opacity: 0.9;
    }

    .cta-group {
      display: flex;
      gap: 1rem;
      justify-content: center;
      margin-top: 2.5rem;
    }

    .pill {
      padding: 0.875rem 2rem;
      border-radius: 2.75rem;
      text-decoration: none;
      font-weight: 600;
      transition: all 0.2s ease;
      font-size: 1rem;
    }

    .pill.primary {
      background: var(--burgundy);
      color: white;
    }

    .pill.secondary {
      background: rgba(255, 255, 255, 0.15);
      color: white;
      backdrop-filter: blur(10px);
      border: 1px solid rgba(255, 255, 255, 0.3);
    }

    .pill:hover {
      transform: translateY(-2px);
      filter: brightness(1.1);
    }

    .content-wrapper {
      max-width: 1000px;
      margin: 0 auto;
      padding: 4rem 2rem;
    }

    .section-header {
      margin-bottom: 3rem;
    }

    .accent-bar {
      width: 60px;
      height: 4px;
      background: var(--burgundy);
      margin-bottom: 1rem;
    }

    h2 {
      font-size: 2.5rem;
      margin: 0;
      color: var(--navy);
    }

    .worship-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
      gap: 2rem;
    }

    .worship-card {
      background: white;
      padding: 2.5rem;
      border-radius: 1rem;
      box-shadow: 0 10px 30px rgba(0,0,0,0.05);
      border-bottom: 4px solid var(--burgundy);
    }

    .worship-card h3 {
      font-size: 1.5rem;
      margin: 0;
      color: var(--burgundy);
    }

    .time {
      font-size: 2rem;
      font-weight: 700;
      margin: 0.5rem 0;
      color: var(--gray-900);
    }

    .desc {
      color: var(--gray-700);
      line-height: 1.6;
      margin-bottom: 0;
    }

    .horizontal-divider {
      height: 1px;
      background: var(--gray-400);
      opacity: 0.3;
      margin: 5rem 0;
    }

    .about-grid {
      display: grid;
      grid-template-columns: 1.5fr 1fr;
      gap: 4rem;
      align-items: start;
    }

    .about-text p {
      font-size: 1.125rem;
      line-height: 1.8;
      color: var(--gray-700);
      margin-bottom: 1.5rem;
    }

    .location-info {
      background: var(--navy);
      color: white;
      padding: 2.5rem;
      border-radius: 1rem;
    }

    .location-info h3 {
      color: var(--gold);
      margin-top: 0;
    }

    address {
      font-style: normal;
      font-size: 1.25rem;
      line-height: 1.6;
      margin-bottom: 2rem;
    }

    .map-link {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      color: white;
      text-decoration: none;
      font-weight: 600;
      padding-top: 1rem;
      border-top: 1px solid rgba(255,255,255,0.2);
    }

    .church-footer {
      padding: 4rem 2rem;
      text-align: center;
      background: white;
    }

    .back-link {
      display: inline-flex;
      align-items: center;
      gap: 0.5rem;
      color: var(--gray-700);
      text-decoration: none;
      font-weight: 500;
      transition: color 0.2s;
    }

    .back-link:hover {
      color: var(--burgundy);
    }

    @media (max-width: 768px) {
      h1 { font-size: 2.5rem; }
      .about-grid { grid-template-columns: 1fr; gap: 3rem; }
      .hero { height: 50vh; }
    }
  `]
})
export class CrossOfChrist {}
