import { Routes } from '@angular/router';
import { Health } from './health';
import { Home } from './home';
import { CrossOfChrist } from './cross-of-christ';
import { UtahCircuit } from './utah-circuit';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'health', component: Health },
  { path: 'cross-of-christ', component: CrossOfChrist },
  { path: 'utah-circuit', component: UtahCircuit }
];
