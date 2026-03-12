import {Routes} from '@angular/router';
import {Health} from './health';
import {Home} from './home';
import {UtahCircuit} from './utah-circuit';
import {CongregationDetail} from './congregation/congregation-detail';
import {EventManagement} from './event/event-management';
import {CongregationPersonAdmin} from './person/congregation-person-admin';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'health', component: Health },
  { path: 'utah-circuit', component: UtahCircuit },
  { path: 'admin/congregation/:slug/events', component: EventManagement },
  { path: 'admin/congregation/:slug/persons', component: CongregationPersonAdmin },
  { path: ':slug', component: CongregationDetail }
];
