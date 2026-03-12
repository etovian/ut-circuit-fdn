import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Person, PersonRelation} from './person.model';

@Injectable({
  providedIn: 'root'
})
export class PersonService {
  private http = inject(HttpClient);
  private apiUrl = '/api/persons';

  getPersons(): Observable<Person[]> {
    return this.http.get<Person[]>(this.apiUrl);
  }

  getPerson(id: number): Observable<Person> {
    return this.http.get<Person>(`${this.apiUrl}/${id}`);
  }

  searchPersons(firstName?: string, lastName?: string): Observable<Person[]> {
    let params = new HttpParams();
    if (firstName) params = params.set('firstName', firstName);
    if (lastName) params = params.set('lastName', lastName);
    return this.http.get<Person[]>(`${this.apiUrl}/search`, { params });
  }

  createPerson(person: Person): Observable<Person> {
    return this.http.post<Person>(this.apiUrl, person);
  }

  updatePerson(id: number, person: Person): Observable<Person> {
    return this.http.put<Person>(`${this.apiUrl}/${id}`, person);
  }

  addPersonToCongregation(congregationId: number, personId: number, position: string): Observable<PersonRelation> {
    const url = `/api/congregations/${congregationId}/persons`;
    return this.http.post<PersonRelation>(url, { personId, position });
  }

  getCongregationPersons(congregationId: number): Observable<PersonRelation[]> {
    return this.http.get<PersonRelation[]>(`/api/congregations/${congregationId}/persons`);
  }

  removePersonFromCongregation(congregationId: number, personId: number): Observable<void> {
    return this.http.delete<void>(`/api/congregations/${congregationId}/persons/${personId}`);
  }

  reorderPersons(congregationId: number, personIds: number[]): Observable<void> {
    return this.http.put<void>(`/api/congregations/${congregationId}/persons/reorder`, personIds);
  }
}
