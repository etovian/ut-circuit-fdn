import {Component, computed, inject, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterLink} from '@angular/router';
import {CongregationComponent} from './congregation/congregation';
import {CongregationService} from './congregation/congregation.service';
import {Congregation} from './congregation/congregation.model';
import {FormsModule} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {ToastService} from './shared/toast.service';

@Component({
  selector: 'app-utah-circuit',
  standalone: true,
  imports: [CommonModule, RouterLink, CongregationComponent, FormsModule],
  templateUrl: './utah-circuit.html',
  styleUrl: './utah-circuit.css'
})
export class UtahCircuit {
  private congregationService = inject(CongregationService);
  private http = inject(HttpClient);
  private toastService = inject(ToastService);
  
  congregations = signal<Congregation[]>([]);
  zipCode = signal('');
  distances = signal<Record<number, number>>({});

  sortedCongregations = computed(() => {
    const list = [...this.congregations()];
    const distMap = this.distances();
    
    if (Object.keys(distMap).length === 0) {
      return list;
    }

    return list.sort((a, b) => {
      const distA = distMap[a.id!] ?? Infinity;
      const distB = distMap[b.id!] ?? Infinity;
      return distA - distB;
    });
  });

  constructor() {
    this.congregationService.getCongregations().subscribe(data => {
      this.congregations.set(data);
    });
  }

  onSearch() {
    const origin = this.zipCode().trim();
    if (!origin || !/^\d{5}$/.test(origin)) {
      this.toastService.error('Please enter a valid 5-digit zip code');
      return;
    }

    const validCongregations = this.congregations().filter(c => 
      c.addresses?.some(a => a.addressType === 'PHYSICAL' && a.address.zipCode)
    );

    const destinations = validCongregations
      .map(c => c.addresses!.find(a => a.addressType === 'PHYSICAL')!.address.zipCode)
      .join('|');

    if (!destinations) {
      this.toastService.error('No congregation addresses found with zip codes.');
      return;
    }

    const url = `/api/distance/matrix?origins=${origin}&destinations=${destinations}`;

    this.http.get<any>(url).subscribe({
      next: (response) => {
        if (response.status === 'OK') {
          const newDistances: Record<number, number> = {};
          const results = response.rows[0].elements;
          
          validCongregations.forEach((congregation, index) => {
            if (congregation.id && results[index] && results[index].status === 'OK') {
              // Convert meters to miles
              newDistances[congregation.id] = results[index].distance.value * 0.000621371;
            }
          });
          
          this.distances.set(newDistances);
        } else {
          console.error('Distance Matrix Error:', response.status, response.error_message || '');
          this.toastService.error(`Distance Calculation Error: ${response.status}. ${response.error_message || 'Please check your configuration.'}`);
        }
      },
      error: (err) => {
        console.error('Failed to connect to distance proxy service:', err);
        this.toastService.error('Failed to connect to the distance service. Please ensure the backend is running and the Google Maps API key is configured.');
      }
    });
  }
}
