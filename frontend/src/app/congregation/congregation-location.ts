import {Component, computed, inject, Input, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Congregation} from './congregation.model';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-congregation-location',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './congregation-location.html',
  styleUrl: './congregation-location.css'
})
export class CongregationLocation {
  private sanitizer = inject(DomSanitizer);

  @Input({ required: true }) set church(value: Congregation | undefined) {
    this._church.set(value);
  }
  get church() { return this._church(); }

  private _church = signal<Congregation | undefined>(undefined);

  googleMapsUrl = computed(() => {
    const church = this._church();
    if (!church) return undefined;

    const physicalAddress = church.addresses?.find(a => a.addressType === 'PHYSICAL');
    if (!physicalAddress) return undefined;

    const addr = physicalAddress.address;
    const query = `${church.name} ${addr.streetAddress} ${addr.city} ${addr.state} ${addr.zipCode}`;
    return `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(query)}`;
  });

  googleMapsEmbedUrl = computed(() => {
    const church = this._church();
    if (!church) return undefined;

    const physicalAddress = church.addresses?.find(a => a.addressType === 'PHYSICAL');
    if (!physicalAddress) return undefined;

    const addr = physicalAddress.address;
    const query = `${church.name} ${addr.streetAddress} ${addr.city} ${addr.state} ${addr.zipCode}`;
    const embedUrl = `https://google.com/maps?q=${encodeURIComponent(query)}&t=&z=13&ie=UTF8&iwloc=&output=embed`;
    return this.sanitizer.bypassSecurityTrustResourceUrl(embedUrl);
  });
}
