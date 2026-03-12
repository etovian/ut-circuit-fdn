import {Component, inject, output, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Subject} from 'rxjs';
import {debounceTime} from 'rxjs/operators';
import {Person} from './person.model';
import {PersonService} from './person.service';

@Component({
  selector: 'app-person-search',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './person-search.html',
  styleUrl: './person-search.css'
})
export class PersonSearch {
  private personService = inject(PersonService);
  private searchSubject = new Subject<{first: string, last: string}>();

  firstName = '';
  lastName = '';
  results = signal<Person[]>([]);
  personSelected = output<Person>();

  constructor() {
    this.searchSubject.pipe(
      debounceTime(300)
    ).subscribe(terms => {
      this.performSearch(terms.first, terms.last);
    });
  }

  onSearchChange() {
    this.searchSubject.next({first: this.firstName, last: this.lastName});
  }

  private performSearch(first: string, last: string) {
    if (!first && !last) {
      this.results.set([]);
      return;
    }

    this.personService.searchPersons(first, last)
      .subscribe(persons => this.results.set(persons));
  }

  selectPerson(person: Person) {
    this.personSelected.emit(person);
    this.results.set([]); // Clear results after selection
    this.firstName = '';
    this.lastName = '';
  }
}
