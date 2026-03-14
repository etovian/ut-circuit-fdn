import {Component, inject, Input} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterLink} from '@angular/router';
import {Congregation} from './congregation.model';
import {ExternalLinkType} from './external-link.model';
import {CongregationService} from './congregation.service';

@Component({
  selector: 'app-congregation-links',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './congregation-links.html',
  styleUrl: './congregation-links.css'
})
export class CongregationLinks {
  @Input({ required: true }) church: Congregation | undefined;

  public ExternalLinkType = ExternalLinkType;
  private congregationService = inject(CongregationService);

  get slug(): string {
    return this.congregationService.getSlug(this.church?.name || '');
  }
}
