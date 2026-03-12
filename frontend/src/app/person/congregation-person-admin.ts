import {Component, inject, signal} from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {CongregationService} from '../congregation/congregation.service';
import {PersonService} from './person.service';
import {Person, PersonRelation} from './person.model';
import {Congregation} from '../congregation/congregation.model';
import {PersonForm} from './person-form';
import {PersonSearch} from './person-search';
import {ToastService} from '../shared/toast.service';

@Component({
  selector: 'app-congregation-person-admin',
  standalone: true,
  imports: [FormsModule, PersonForm, PersonSearch, RouterLink],
  templateUrl: './congregation-person-admin.html',
  styleUrl: './congregation-person-admin.css'
})
export class CongregationPersonAdmin {
  private route = inject(ActivatedRoute);
  private congregationService = inject(CongregationService);
  private personService = inject(PersonService);
  private toastService = inject(ToastService);

  congregation = signal<Congregation | undefined>(undefined);
  slug = signal<string | null>(null);
  selectedPerson = signal<Person>(this.emptyPerson());
  congregationPersons = signal<PersonRelation[]>([]);
  position = '';
  isModalOpen = signal<boolean>(false);

  constructor() {
    const s = this.route.snapshot.paramMap.get('slug');
    this.slug.set(s);
    if (s) {
      this.congregationService.getCongregationBySlug(s).subscribe(c => {
        this.congregation.set(c);
        if (c && c.id) {
          this.loadCongregationPersons(c.id);
        }
      });
    }
  }

  loadCongregationPersons(congregationId: number) {
    this.personService.getCongregationPersons(congregationId).subscribe(persons => {
      this.congregationPersons.set(persons);
    });
  }

  onPersonSelected(person: Person) {
    this.selectedPerson.set({...person});
    
    // Set position if the person is already associated with this congregation
    const currentCongregationId = this.congregation()?.id;
    if (currentCongregationId && person.congregations) {
      const relation = person.congregations.find(c => c.id === currentCongregationId);
      this.position = relation ? relation.position : '';
    } else {
      this.position = '';
    }
    this.isModalOpen.set(true);
  }

  onSave(person: Person) {
    const c = this.congregation();
    if (!c || !c.id) return;

    const personObs = person.id 
      ? this.personService.updatePerson(person.id, person)
      : this.personService.createPerson(person);

    personObs.subscribe(savedPerson => {
      if (savedPerson.id) {
        this.personService.addPersonToCongregation(c.id!, savedPerson.id, this.position)
          .subscribe(() => {
            this.toastService.success('Person details and congregation association saved successfully!');
            this.loadCongregationPersons(c.id!);
            this.closeModal();
          });
      }
    });
  }

  onEdit(relation: PersonRelation) {
    this.personService.getPerson(relation.id).subscribe(person => {
      this.selectedPerson.set(person);
      this.position = relation.position;
      this.isModalOpen.set(true);
    });
  }

  onDelete(relation: PersonRelation) {
    const c = this.congregation();
    if (!c || !c.id) return;

    if (confirm(`Are you sure you want to remove ${relation.firstName} ${relation.lastName} from this congregation?`)) {
      this.personService.removePersonFromCongregation(c.id, relation.id).subscribe(() => {
        this.loadCongregationPersons(c.id!);
        if (this.selectedPerson().id === relation.id) {
          this.closeModal();
        }
      });
    }
  }

  moveUp(index: number) {
    if (index === 0) return;
    const list = [...this.congregationPersons()];
    [list[index - 1], list[index]] = [list[index], list[index - 1]];
    this.updateOrder(list);
  }

  moveDown(index: number) {
    const list = [...this.congregationPersons()];
    if (index === list.length - 1) return;
    [list[index], list[index + 1]] = [list[index + 1], list[index]];
    this.updateOrder(list);
  }

  private updateOrder(list: PersonRelation[]) {
    const c = this.congregation();
    if (!c || !c.id) return;

    this.congregationPersons.set(list);
    const personIds = list.map(p => p.id);
    this.personService.reorderPersons(c.id, personIds).subscribe();
  }

  openCreateModal() {
    this.selectedPerson.set(this.emptyPerson());
    this.position = '';
    this.isModalOpen.set(true);
  }

  closeModal() {
    this.selectedPerson.set(this.emptyPerson());
    this.position = '';
    this.isModalOpen.set(false);
  }

  private emptyPerson(): Person {
    return {
      firstName: '',
      lastName: ''
    };
  }
}
