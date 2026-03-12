import {Component, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ToastService} from './toast.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="toast-container">
      @for (toast of toastService.toasts(); track toast.id) {
        <div class="toast" [ngClass]="toast.type" (click)="toastService.remove(toast.id)">
          <div class="toast-message">{{ toast.message }}</div>
          <button class="toast-close">✕</button>
        </div>
      }
    </div>
  `,
  styleUrl: './toast.css'
})
export class ToastComponent {
  toastService = inject(ToastService);
}
