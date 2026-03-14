import {Component, computed, Input, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Congregation} from './congregation.model';

@Component({
  selector: 'app-congregation-location',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './congregation-location.html',
  styleUrl: './congregation-location.css'
})
export class CongregationLocation {
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
}
