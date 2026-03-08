import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map, Observable} from 'rxjs';
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
      map(c => c ? this.enrichCongregation(c) : undefined)
    );
  }

  private getSlug(name: string): string {
    return name.toLowerCase().replace(/\s+/g, '-').replace(/[()]/g, '');
  }

  private enrichCongregation(c: Congregation): Congregation {
    const slug = this.getSlug(c.name);

    // For now, let's hardcode the extra details for Cross of Christ
    // In a real app, these would come from the database
    if (slug === 'cross-of-christ') {
      return {
        ...c,
        tagline: '"Sharing the love of Christ with Bountiful since 1958"',
        location: 'Bountiful, Utah',
        address: '184 N 200 W, Bountiful, UT 84010',
        googleMapsLink: 'https://www.google.com/maps/search/?api=1&query=Cross+of+Christ+Lutheran+Church+Bountiful+UT',
        worshipTimes: [
          { title: 'Sunday Service', time: '10:30 AM', description: 'A traditional Lutheran service with communion and fellowship.' },
          { title: 'Bible Study', time: '9:15 AM', description: 'Deep dive into scripture and congregational learning.' }
        ]
      };
    }

    // Default enrichment for others
    const locationMatch = c.description?.match(/Located in ([^.]+)/);
    return {
      ...c,
      location: locationMatch ? locationMatch[1] : 'Utah',
      tagline: `Serving the ${locationMatch ? locationMatch[1] : 'Utah'} community`,
      worshipTimes: [
        { title: 'Sunday Service', time: '10:00 AM', description: 'Join us for worship and fellowship.' }
      ]
    };
  }
}
