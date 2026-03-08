import {Component, inject, signal} from '@angular/core';
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
