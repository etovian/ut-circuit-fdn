import {Component, inject, output, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Subject} from 'rxjs';
import {debounceTime, distinctUntilChanged} from 'rxjs/operators';
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
  private searchSubject = new Subject<void>();

  firstName = '';
  lastName = '';
  results = signal<Person[]>([]);
  personSelected = output<Person>();

  constructor() {
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(() => {
      this.performSearch();
    });
  }

  onSearchChange() {
    this.searchSubject.next();
  }

  private performSearch() {
    if (!this.firstName && !this.lastName) {
      this.results.set([]);
      return;
    }

    this.personService.searchPersons(this.firstName, this.lastName)
      .subscribe(persons => this.results.set(persons));
  }

  selectPerson(person: Person) {
    this.personSelected.emit(person);
    this.results.set([]); // Clear results after selection
    this.firstName = '';
    this.lastName = '';
  }
}
