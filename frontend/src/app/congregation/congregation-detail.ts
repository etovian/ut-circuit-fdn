import {Component, inject, OnDestroy, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {CongregationService} from './congregation.service';
import {Congregation, ScheduledEvent} from './congregation.model';
import {CongregationHero} from './congregation-hero';
import {CongregationEvents} from './congregation-events';
import {CongregationAbout} from './congregation-about';
import {CongregationLocation} from './congregation-location';
import {CongregationLinks} from './congregation-links';
import {CongregationLeadership} from './congregation-leadership';

@Component({
  selector: 'app-congregation-detail',
  standalone: true,
  imports: [
    CommonModule, 
    RouterLink, 
    CongregationHero, 
    CongregationEvents, 
    CongregationAbout, 
    CongregationLocation,
    CongregationLinks, 
    CongregationLeadership
  ],
  templateUrl: './congregation-detail.html',
  styleUrl: './congregation-detail.css'
})
export class CongregationDetail implements OnDestroy {
  private route = inject(ActivatedRoute);
  private congregationService = inject(CongregationService);

  congregation = signal<Congregation | undefined>(undefined);
  scheduledEvents = signal<ScheduledEvent[]>([]);
  activeTab = signal<string>('events');

  constructor() {
    this.route.params.subscribe(params => {
      const slug = params['slug'];
      if (slug) {
        this.congregationService.getCongregationBySlug(slug).subscribe(data => {
          this.congregation.set(data);

          if (data && data.id) {
            this.congregationService.getScheduledEventsNextSevenDays(data.id).subscribe(events => {
              this.scheduledEvents.set(events);
            });
          }
        });
      }
    });
  }

  setActiveTab(tab: string) {
    this.activeTab.set(tab);
  }

  ngOnDestroy() {
  }
}
