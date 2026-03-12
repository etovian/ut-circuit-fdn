import {Component, computed, inject, OnDestroy, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {CongregationService} from './congregation.service';
import {Congregation, PersonRelation, ScheduledEvent} from './congregation.model';
import {interval, Subscription} from 'rxjs';
import {CdkDragDrop, DragDropModule, moveItemInArray} from '@angular/cdk/drag-drop';

const DEFAULT_HERO_PHOTO_URL = 'https://images.unsplash.com/photo-1548625149-fc4a29cf7092?auto=format&fit=crop&q=80&w=1600';
const PHOTO_ROTATION_INTERVAL_MS = 5000;

@Component({
  selector: 'app-congregation-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, DragDropModule],
  templateUrl: './congregation-detail.html',
  styleUrl: './congregation-detail.css'
})
export class CongregationDetail implements OnDestroy {
  private route = inject(ActivatedRoute);
  private congregationService = inject(CongregationService);
  public congregationServicePublic = this.congregationService; // For template access

  congregation = signal<Congregation | undefined>(undefined);
  scheduledEvents = signal<ScheduledEvent[]>([]);
  currentPhotoIndex = signal(0);
  private rotationSubscription?: Subscription;

  currentPhotoUrl = computed(() => {
    const church = this.congregation();
    if (!church || !church.bannerPhotos || church.bannerPhotos.length === 0) {
      return DEFAULT_HERO_PHOTO_URL;
    }
    return church.bannerPhotos[this.currentPhotoIndex() % church.bannerPhotos.length];
  });

  googleMapsUrl = computed(() => {
    const church = this.congregation();
    if (!church) return undefined;

    const physicalAddress = church.addresses?.find(a => a.addressType === 'PHYSICAL');
    if (!physicalAddress) return undefined;

    const addr = physicalAddress.address;
    const query = `${church.name} ${addr.streetAddress} ${addr.city} ${addr.state} ${addr.zipCode}`;
    return `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(query)}`;
  });

  constructor() {
    this.route.params.subscribe(params => {
      const slug = params['slug'];
      if (slug) {
        this.congregationService.getCongregationBySlug(slug).subscribe(data => {
          this.congregation.set(data);
          this.currentPhotoIndex.set(0);
          this.startPhotoRotation();

          if (data && data.id) {
            this.congregationService.getScheduledEventsNextSevenDays(data.id).subscribe(events => {
              this.scheduledEvents.set(events);
            });
          }
        });
      }
    });
  }

  drop(event: CdkDragDrop<PersonRelation[]>) {
    const church = this.congregation();
    if (!church || !church.persons || !church.id) return;

    const persons = [...church.persons];
    moveItemInArray(persons, event.previousIndex, event.currentIndex);
    
    // Update local state immediately for responsiveness
    this.congregation.update(c => c ? { ...c, persons } : undefined);

    // Persist to backend
    const personIds = persons.map(p => p.id);
    this.congregationService.reorderPersons(church.id, personIds).subscribe({
      error: (err) => {
        console.error('Failed to reorder persons', err);
        // Rollback on error
        this.congregation.set(church);
      }
    });
  }

  private startPhotoRotation() {
    if (this.rotationSubscription) {
      this.rotationSubscription.unsubscribe();
    }

    const church = this.congregation();
    if (church && church.bannerPhotos && church.bannerPhotos.length > 1) {
      this.rotationSubscription = interval(PHOTO_ROTATION_INTERVAL_MS).subscribe(() => {
        this.currentPhotoIndex.update(index => index + 1);
      });
    }
  }

  ngOnDestroy() {
    if (this.rotationSubscription) {
      this.rotationSubscription.unsubscribe();
    }
  }
}
