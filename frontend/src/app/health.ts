import { Component, inject, signal, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-health',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <main class="main">
      <div class="content">
        <div class="left-side">
          <h1>System Health</h1>
          <p>Real-time status of the backend circuit foundation. ⚡</p>
          
          <div class="status-card" [class.up]="healthStatus()?.status === 'UP'">
            <div class="status-indicator"></div>
            <div class="status-text">
              <span class="label">Backend Status</span>
              <span class="value">{{ healthStatus()?.status || 'CHECKING...' }}</span>
            </div>
          </div>

          <div *ngIf="errorMessage()" class="error-message">
            <svg xmlns="http://www.w3.org/2000/svg" height="20" viewBox="0 -960 960 960" width="20" fill="currentColor">
              <path d="M480-280q17 0 28.5-11.5T520-320q0-17-11.5-28.5T480-360q-17 0-28.5 11.5T440-320q0 17 11.5 28.5T480-280Zm-40-160h80v-240h-80v240Zm40 360q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Z"/>
            </svg>
            {{ errorMessage() }}
          </div>
        </div>

        <div class="divider" role="separator"></div>

        <div class="right-side">
          <div class="pill-group">
            <button class="pill primary" (click)="checkHealth()">
              <span>Refresh Status</span>
              <svg xmlns="http://www.w3.org/2000/svg" height="18" viewBox="0 -960 960 960" width="18" fill="currentColor">
                <path d="M480-160q-134 0-227-93t-93-227q0-134 93-227t227-93q69 0 132 28.5T720-690v-110h80v280H520v-80h168q-32-56-87.5-88T480-720q-100 0-170 70t-70 170q0 100 70 170t170 70q77 0 139-44t87-116h84q-28 106-114 173t-196 67Z"/>
              </svg>
            </button>
            <a class="pill" routerLink="/">
              <span>Back to Home</span>
              <svg xmlns="http://www.w3.org/2000/svg" height="18" viewBox="0 -960 960 960" width="18" fill="currentColor">
                <path d="M240-200h120v-240h240v240h120v-360L480-740 240-560v360Zm-80 80v-480l320-240 320 240v480H520v-240h-80v240H160Z"/>
              </svg>
            </a>
          </div>
        </div>
      </div>
    </main>
  `,
  styles: [`
    :host {
      --bright-blue: oklch(51.01% 0.274 263.83);
      --electric-violet: oklch(53.18% 0.28 296.97);
      --french-violet: oklch(47.66% 0.246 305.88);
      --vivid-pink: oklch(69.02% 0.277 332.77);
      --hot-red: oklch(61.42% 0.238 15.34);
      --orange-red: oklch(63.32% 0.24 31.68);
      --gray-900: oklch(19.37% 0.006 300.98);
      --gray-700: oklch(36.98% 0.014 302.71);
      --gray-400: oklch(70.9% 0.015 304.04);
      --red-to-pink-to-purple-vertical-gradient: linear-gradient(180deg, var(--orange-red) 0%, var(--vivid-pink) 50%, var(--electric-violet) 100%);
      --red-to-pink-to-purple-horizontal-gradient: linear-gradient(90deg, var(--orange-red) 0%, var(--vivid-pink) 50%, var(--electric-violet) 100%);
      
      font-family: "Inter", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;
      display: block;
      height: 100dvh;
    }

    .main {
      width: 100%;
      min-height: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 1rem;
      box-sizing: border-box;
    }

    .content {
      display: flex;
      justify-content: space-around;
      align-items: center;
      width: 100%;
      max-width: 800px;
    }

    h1 {
      font-size: 3.125rem;
      color: var(--gray-900);
      font-weight: 500;
      line-height: 100%;
      letter-spacing: -0.125rem;
      margin: 0;
      font-family: "Inter Tight", sans-serif;
    }

    p {
      margin-top: 1.5rem;
      color: var(--gray-700);
      font-size: 1.1rem;
    }

    .status-card {
      margin-top: 2.5rem;
      padding: 1.5rem;
      background: #f8f9fa;
      border-radius: 1rem;
      border: 1px solid #e9ecef;
      display: flex;
      align-items: center;
      gap: 1.25rem;
      transition: all 0.3s ease;
    }

    .status-card.up {
      background: color-mix(in srgb, #28a745 5%, transparent);
      border-color: color-mix(in srgb, #28a745 20%, transparent);
    }

    .status-indicator {
      width: 12px;
      height: 12px;
      border-radius: 50%;
      background: var(--gray-400);
      box-shadow: 0 0 0 4px color-mix(in srgb, var(--gray-400) 20%, transparent);
    }

    .up .status-indicator {
      background: #28a745;
      box-shadow: 0 0 0 4px color-mix(in srgb, #28a745 20%, transparent);
      animation: pulse 2s infinite;
    }

    @keyframes pulse {
      0% { box-shadow: 0 0 0 0px color-mix(in srgb, #28a745 40%, transparent); }
      70% { box-shadow: 0 0 0 10px color-mix(in srgb, #28a745 0%, transparent); }
      100% { box-shadow: 0 0 0 0px color-mix(in srgb, #28a745 0%, transparent); }
    }

    .status-text {
      display: flex;
      flex-direction: column;
    }

    .label {
      font-size: 0.75rem;
      text-transform: uppercase;
      letter-spacing: 0.05rem;
      color: var(--gray-700);
      font-weight: 600;
    }

    .value {
      font-size: 1.5rem;
      font-weight: 700;
      color: var(--gray-900);
    }

    .up .value {
      color: #1e7e34;
    }

    .error-message {
      margin-top: 1.5rem;
      color: var(--hot-red);
      background: color-mix(in srgb, var(--hot-red) 5%, transparent);
      padding: 0.75rem 1rem;
      border-radius: 0.5rem;
      font-size: 0.875rem;
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }

    .divider {
      width: 1px;
      height: 200px;
      background: var(--red-to-pink-to-purple-vertical-gradient);
      margin-inline: 3rem;
    }

    .pill-group {
      display: flex;
      flex-direction: column;
      gap: 1rem;
    }

    .pill {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 1rem;
      padding: 0.75rem 1.25rem;
      border-radius: 2.75rem;
      border: 0;
      background: color-mix(in srgb, var(--bright-blue) 5%, transparent);
      color: var(--bright-blue);
      font-weight: 500;
      text-decoration: none;
      cursor: pointer;
      transition: all 0.2s ease;
      font-size: 0.875rem;
      min-width: 180px;
    }

    .pill:hover {
      background: color-mix(in srgb, var(--bright-blue) 15%, transparent);
      transform: translateY(-1px);
    }

    .pill.primary {
      background: var(--bright-blue);
      color: white;
    }

    .pill.primary:hover {
      background: color-mix(in srgb, var(--bright-blue) 90%, black);
    }

    @media screen and (max-width: 650px) {
      .content { flex-direction: column; text-align: center; }
      .divider { height: 1px; width: 100%; margin-block: 2.5rem; background: var(--red-to-pink-to-purple-horizontal-gradient); }
      .status-card { justify-content: center; }
      .pill-group { align-items: center; }
    }
  `]
})
export class Health implements OnInit {
  private http = inject(HttpClient);
  healthStatus = signal<{ status: string } | null>(null);
  errorMessage = signal<string | null>(null);

  ngOnInit() { this.checkHealth(); }

  checkHealth() {
    this.errorMessage.set(null);
    this.http.get<{ status: string }>('/api/health').subscribe({
      next: (data) => this.healthStatus.set(data),
      error: (err) => {
        this.errorMessage.set('Backend connection failed');
        this.healthStatus.set({ status: 'OFFLINE' });
      }
    });
  }
}
