import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map, Observable, of, switchMap} from 'rxjs';
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
    return this.http.get<Congregation>(`${this.apiUrl}/${id}`).pipe(
      map(c => this.enrichCongregation(c))
    );
  }

  getCongregationBySlug(slug: string): Observable<Congregation | undefined> {
    return this.getCongregations().pipe(
      map(list => list.find(c => this.getSlug(c.name) === slug)),
      switchMap(c => c && c.id ? this.getCongregationById(c.id) : of(undefined))
    );
  }

  private getSlug(name: string): string {
    return name.toLowerCase().replace(/\s+/g, '-').replace(/[()]/g, '');
  }

  private enrichCongregation(c: Congregation): Congregation {
    // For now, let's hardcode the extra details for Cross of Christ (ID 1)
    // In a real app, these would come from the database
    if (c.id === 1) {
      return {
        ...c,
        tagline: '"Sharing the love of Christ with Bountiful since 1958"',
        location: 'Bountiful, Utah',
        worshipTimes: [
          { title: 'Sunday Service', time: '10:30 AM', description: 'A traditional Lutheran service with communion and fellowship.' },
          { title: 'Bible Study', time: '9:15 AM', description: 'Deep dive into scripture and congregational learning.' }
        ]
      };
    }

    // Default enrichment for others
    const locationMatch = c.description?.match(/Located in ([^.]+)/);
    const city = locationMatch ? locationMatch[1] : 'Utah';
    return {
      ...c,
      location: city,
      tagline: `Serving the ${city} community`,
      worshipTimes: [
        { title: 'Sunday Service', time: '10:00 AM', description: 'Join us for worship and fellowship.' }
      ]
    };
  }
}
