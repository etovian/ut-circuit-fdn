import {Component, inject, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterLink} from '@angular/router';
import {EventService} from './event.service';
import {ScheduledEventInstance} from './event.model';

@Component({
  selector: 'app-circuit-events',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './circuit-events.html',
  styleUrl: './circuit-events.css'
})
export class CircuitEvents {
  private eventService = inject(EventService);
  events = signal<ScheduledEventInstance[]>([]);

  constructor() {
    this.eventService.getCircuitEvents().subscribe(data => {
      this.events.set(data);
    });
  }
}
