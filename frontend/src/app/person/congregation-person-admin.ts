import {Component, inject, signal} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {CongregationService} from '../congregation/congregation.service';
import {PersonService} from './person.service';
import {Person} from './person.model';
import {Congregation} from '../congregation/congregation.model';
import {PersonForm} from './person-form';
import {PersonSearch} from './person-search';

@Component({
  selector: 'app-congregation-person-admin',
  standalone: true,
  imports: [FormsModule, PersonForm, PersonSearch],
  templateUrl: './congregation-person-admin.html',
  styleUrl: './congregation-person-admin.css'
})
export class CongregationPersonAdmin {
  private route = inject(ActivatedRoute);
  private congregationService = inject(CongregationService);
  private personService = inject(PersonService);

  congregation = signal<Congregation | undefined>(undefined);
  selectedPerson = signal<Person>(this.emptyPerson());
  position = '';

  constructor() {
    const slug = this.route.snapshot.paramMap.get('slug');
    if (slug) {
      this.congregationService.getCongregationBySlug(slug).subscribe(c => {
        this.congregation.set(c);
      });
    }
  }

  onPersonSelected(person: Person) {
    this.selectedPerson.set({...person});
  }

  onSave(person: Person) {
    const c = this.congregation();
    if (!c || !c.id) return;

    this.personService.addPersonToCongregation(c.id, person, this.position)
      .subscribe(() => {
        alert('Person associated with congregation successfully!');
        this.resetForm();
      });
  }

  resetForm() {
    this.selectedPerson.set(this.emptyPerson());
    this.position = '';
  }

  private emptyPerson(): Person {
    return {
      firstName: '',
      lastName: ''
    };
  }
}
