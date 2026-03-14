import {Component, inject, Input, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterLink} from '@angular/router';
import {CdkDragDrop, DragDropModule, moveItemInArray} from '@angular/cdk/drag-drop';
import {Congregation, PersonRelation} from './congregation.model';
import {CongregationService} from './congregation.service';

@Component({
  selector: 'app-congregation-leadership',
  standalone: true,
  imports: [CommonModule, RouterLink, DragDropModule],
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

  get slug(): string {
    return this.congregationService.getSlug(this.church?.name || '');
  }

  drop(event: CdkDragDrop<PersonRelation[]>) {
    const church = this.church;
    if (!church || !church.persons || !church.id) return;

    const persons = [...church.persons];
    moveItemInArray(persons, event.previousIndex, event.currentIndex);

    // Update local state immediately for responsiveness
    this._church.update(c => c ? { ...c, persons } : undefined);

    // Persist to backend
    const personIds = persons.map(p => p.id);
    this.congregationService.reorderPersons(church.id, personIds).subscribe({
      error: (err) => {
        console.error('Failed to reorder persons', err);
        // Rollback on error
        this._church.set(church);
      }
    });
  }
}
