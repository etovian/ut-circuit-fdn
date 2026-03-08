import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Congregation} from './congregation.model';

@Injectable({
  providedIn: 'root'
})
export class CongregationService {
  private http = inject(HttpClient);
  private apiUrl = '/api/congregations';

  getCongregations(): Observable<Congregation[]> {
    return this.http.get<Congregation[]>(this.apiUrl);
  }

  getCongregationById(id: number): Observable<Congregation> {
    return this.http.get<Congregation>(`${this.apiUrl}/${id}`);
  }
}
