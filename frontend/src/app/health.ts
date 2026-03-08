import {Component, inject, OnInit, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CommonModule} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-health',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './health.html',
  styleUrl: './health.css'
})
export class Health implements OnInit {
  private http = inject(HttpClient);
  healthStatus = signal<{ status: string } | null>(null);
  healthHistory = signal<Array<{ id: number, checkTime: string, status: string }>>([]);
  errorMessage = signal<string | null>(null);
  isRefreshing = signal<boolean>(false);

  ngOnInit() {
    this.checkHealth();
    this.fetchHistory();
  }

  checkHealth() {
    this.errorMessage.set(null);
    this.isRefreshing.set(true);
    this.http.get<{ status: string }>('/api/health').subscribe({
      next: (data) => {
        this.healthStatus.set(data);
        this.fetchHistory();
        this.isRefreshing.set(false);
      },
      error: (err) => {
        this.errorMessage.set('Backend connection failed');
        this.healthStatus.set({ status: 'OFFLINE' });
        this.isRefreshing.set(false);
      }
    });
  }

  fetchHistory() {
    this.http.get<Array<{ id: number, checkTime: string, status: string }>>('/api/health/history').subscribe({
      next: (data) => this.healthHistory.set(data),
      error: (err) => console.error('Failed to fetch history', err)
    });
  }
}
