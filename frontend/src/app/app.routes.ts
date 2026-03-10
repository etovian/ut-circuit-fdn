import {Routes} from '@angular/router';
import {Health} from './health';
import {Home} from './home';
import {UtahCircuit} from './utah-circuit';
import {CongregationDetail} from './congregation/congregation-detail';
import {EventManagement} from './event/event-management';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'health', component: Health },
  { path: 'utah-circuit', component: UtahCircuit },
  { path: 'admin/congregation/:slug/events', component: EventManagement },
  { path: ':slug', component: CongregationDetail }
];
