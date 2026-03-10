import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {EventTemplate, ScheduledEventInstance} from './event.model';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private http = inject(HttpClient);
  private apiUrl = '/api/events';

  // --- Public Feed ---

  getScheduledInstances(congregationId: number, start: string, end: string): Observable<ScheduledEventInstance[]> {
    return this.http.get<ScheduledEventInstance[]>(`${this.apiUrl}/scheduled?congregationId=${congregationId}&start=${start}&end=${end}`);
  }

  // --- Administrative ---

  getTemplates(congregationId?: number): Observable<EventTemplate[]> {
    const params: any = {};
    if (congregationId) params.congregationId = congregationId;
    return this.http.get<EventTemplate[]>(`${this.apiUrl}/templates`, { params });
  }

  createTemplate(template: EventTemplate): Observable<EventTemplate> {
    return this.http.post<EventTemplate>(`${this.apiUrl}/templates`, template);
  }

  updateTemplate(id: number, template: EventTemplate): Observable<EventTemplate> {
    return this.http.put<EventTemplate>(`${this.apiUrl}/templates/${id}`, template);
  }

  deleteTemplate(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/templates/${id}`);
  }

  cancelInstance(instanceId: number): Observable<ScheduledEventInstance> {
    return this.http.patch<ScheduledEventInstance>(`${this.apiUrl}/scheduled/${instanceId}/cancel`, {});
  }

  overrideInstance(instanceId: number, instance: Partial<ScheduledEventInstance>): Observable<ScheduledEventInstance> {
    return this.http.put<ScheduledEventInstance>(`${this.apiUrl}/scheduled/${instanceId}/override`, instance);
  }
}
