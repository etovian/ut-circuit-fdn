import {Component, Input} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterLink} from '@angular/router';
import {Congregation} from './congregation.model';

@Component({
  selector: 'app-congregation-card',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './congregation.html',
  styleUrl: './congregation.css'
})
export class CongregationComponent {
  @Input({required: true}) congregation!: Congregation;

  get slug(): string {
    return this.congregation.name.toLowerCase().replace(/\s+/g, '-').replace(/[()]/g, '');
  }

  get city(): string {
    if (this.congregation.addresses && this.congregation.addresses.length > 0) {
      return this.congregation.addresses[0].address.city;
    }
    if (this.congregation.description?.startsWith('Located in ')) {
      return this.congregation.description
        .replace('Located in ', '')
        .split('.')[0];
    }
    return 'Utah';
  }

  get streetAddress(): string {
    if (this.congregation.addresses && this.congregation.addresses.length > 0) {
      const addr = this.congregation.addresses[0].address;
      return addr.streetAddress + (addr.addressLine2 ? ', ' + addr.addressLine2 : '');
    }
    return '';
  }

  get cityStateZip(): string {
    if (this.congregation.addresses && this.congregation.addresses.length > 0) {
      const addr = this.congregation.addresses[0].address;
      return `${addr.city}, ${addr.state} ${addr.zipCode}`;
    }
    return this.city;
  }
}
