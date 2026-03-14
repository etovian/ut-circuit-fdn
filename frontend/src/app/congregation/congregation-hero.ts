import {Component, computed, Input, OnDestroy, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Congregation} from './congregation.model';
import {interval, Subscription} from 'rxjs';

const DEFAULT_HERO_PHOTO_URL = 'https://images.unsplash.com/photo-1548625149-fc4a29cf7092?auto=format&fit=crop&q=80&w=1600';
const PHOTO_ROTATION_INTERVAL_MS = 5000;

@Component({
  selector: 'app-congregation-hero',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './congregation-hero.html',
  styleUrl: './congregation-hero.css'
})
export class CongregationHero implements OnDestroy {
  @Input({ required: true }) set church(value: Congregation | undefined) {
    this._church.set(value);
    this.currentPhotoIndex.set(0);
    this.startPhotoRotation();
  }
  get church(): Congregation | undefined { return this._church(); }

  get location(): string {
    const church = this._church();
    if (!church) return '';
    if (church.addresses && church.addresses.length > 0) {
      const addr = church.addresses[0].address;
      return `${addr.city}, ${addr.state}`;
    }
    if (church.location) return church.location;

    return '';
  }

  private _church = signal<Congregation | undefined>(undefined);
  currentPhotoIndex = signal(0);
  private rotationSubscription?: Subscription;

  currentPhotoUrl = computed(() => {
    const church = this._church();
    if (!church || !church.bannerPhotos || church.bannerPhotos.length === 0) {
      return DEFAULT_HERO_PHOTO_URL;
    }
    return church.bannerPhotos[this.currentPhotoIndex() % church.bannerPhotos.length];
  });

  private startPhotoRotation() {
    if (this.rotationSubscription) {
      this.rotationSubscription.unsubscribe();
    }

    const church = this._church();
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
