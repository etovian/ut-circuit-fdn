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

  // Check if we have a dedicated page for this congregation
  // For now, let's assume only cross-of-christ has one
  get hasPage(): boolean {
    return this.slug === 'cross-of-christ';
  }

  get location(): string {
    // Description often starts with "Located in Bountiful, Utah."
    // Let's try to extract the location from the description if it follows that pattern
    if (this.congregation.description?.startsWith('Located in ')) {
      return this.congregation.description
        .replace('Located in ', '')
        .split('.')[0];
    }
    // Fallback if not matching the pattern
    return 'Utah';
  }
}
