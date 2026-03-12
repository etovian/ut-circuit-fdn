import {Injectable, signal} from '@angular/core';
import {Toast, ToastType} from './toast.model';

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  toasts = signal<Toast[]>([]);
  private nextId = 1;

  success(message: string, duration?: number) {
    this.add(message, 'success', duration);
  }

  error(message: string, duration?: number) {
    this.add(message, 'error', duration);
  }

  info(message: string, duration?: number) {
    this.add(message, 'info', duration);
  }

  warning(message: string, duration?: number) {
    this.add(message, 'warning', duration);
  }

  private add(message: string, type: ToastType, duration = 5000) {
    const id = this.nextId++;
    const toast: Toast = { id, message, type, duration };
    
    this.toasts.update(current => [...current, toast]);

    if (duration > 0) {
      setTimeout(() => this.remove(id), duration);
    }
  }

  remove(id: number) {
    this.toasts.update(current => current.filter(t => t.id !== id));
  }
}
