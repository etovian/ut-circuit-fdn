import {Component, inject, Input} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterLink} from '@angular/router';
import {ScheduledEvent} from './congregation.model';
import {CongregationService} from './congregation.service';

@Component({
  selector: 'app-congregation-events',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './congregation-events.html',
  styleUrl: './congregation-events.css'
})
export class CongregationEvents {
  @Input({ required: true }) events: ScheduledEvent[] = [];
  @Input({ required: true }) churchName: string = '';

  private congregationService = inject(CongregationService);

  get slug(): string {
    return this.congregationService.getSlug(this.churchName);
  }
}
