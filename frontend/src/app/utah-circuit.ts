import {Component, inject, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterLink} from '@angular/router';
import {CongregationComponent} from './congregation/congregation';
import {CongregationService} from './congregation/congregation.service';
import {Congregation} from './congregation/congregation.model';

@Component({
  selector: 'app-utah-circuit',
  standalone: true,
  imports: [CommonModule, RouterLink, CongregationComponent],
  templateUrl: './utah-circuit.html',
  styleUrl: './utah-circuit.css'
})
export class UtahCircuit {
  private congregationService = inject(CongregationService);
  congregations = signal<Congregation[]>([]);

  constructor() {
    this.congregationService.getCongregations().subscribe(data => {
      this.congregations.set(data);
    });
  }
}
