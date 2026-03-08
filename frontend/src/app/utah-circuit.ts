import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-utah-circuit',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './utah-circuit.html',
  styleUrl: './utah-circuit.css'
})
export class UtahCircuit {}
