import {Component, inject, Input, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterLink} from '@angular/router';
import {Congregation, PersonRelation} from './congregation.model';
import {CongregationService} from './congregation.service';

@Component({
  selector: 'app-congregation-leadership',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './congregation-leadership.html',
  styleUrl: './congregation-leadership.css'
})
export class CongregationLeadership {
  @Input({ required: true }) set church(value: Congregation | undefined) {
    this._church.set(value);
  }
  get church() { return this._church(); }

  private _church = signal<Congregation | undefined>(undefined);
  private congregationService = inject(CongregationService);
  selectedPerson = signal<PersonRelation | null>(null);

  get slug(): string {
    return this.congregationService.getSlug(this.church?.name || '');
  }

  openModal(person: PersonRelation) {
    this.selectedPerson.set(person);
    document.body.style.overflow = 'hidden';
  }

  closeModal() {
    this.selectedPerson.set(null);
    document.body.style.overflow = 'auto';
  }
}
