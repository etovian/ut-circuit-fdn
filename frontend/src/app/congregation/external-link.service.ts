import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CongregationExternalLink} from './external-link.model';

@Injectable({
  providedIn: 'root'
})
export class ExternalLinkService {
  private http = inject(HttpClient);
  private apiUrl = '/api/congregations';

  getLinks(congregationId: number): Observable<CongregationExternalLink[]> {
    return this.http.get<CongregationExternalLink[]>(`${this.apiUrl}/${congregationId}/links`);
  }

  createLink(congregationId: number, link: CongregationExternalLink): Observable<CongregationExternalLink> {
    return this.http.post<CongregationExternalLink>(`${this.apiUrl}/${congregationId}/links`, link);
  }

  updateLink(congregationId: number, linkId: number, link: CongregationExternalLink): Observable<CongregationExternalLink> {
    return this.http.put<CongregationExternalLink>(`${this.apiUrl}/${congregationId}/links/${linkId}`, link);
  }

  deleteLink(congregationId: number, linkId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${congregationId}/links/${linkId}`);
  }

  reorderLinks(congregationId: number, linkIds: number[]): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${congregationId}/links/reorder`, linkIds);
  }
}
