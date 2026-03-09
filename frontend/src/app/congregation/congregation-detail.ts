import {Component, computed, inject, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {CongregationService} from './congregation.service';
import {Congregation} from './congregation.model';

@Component({
  selector: 'app-congregation-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './congregation-detail.html',
  styleUrl: './congregation-detail.css'
})
export class CongregationDetail {
  private route = inject(ActivatedRoute);
  private congregationService = inject(CongregationService);

  congregation = signal<Congregation | undefined>(undefined);

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
        });
      }
    });
  }
}
