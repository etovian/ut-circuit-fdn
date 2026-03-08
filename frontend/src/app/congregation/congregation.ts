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

  get location(): string {
    if (this.congregation.description?.startsWith('Located in ')) {
      return this.congregation.description
        .replace('Located in ', '')
        .split('.')[0];
    }
    return 'Utah';
  }
}
