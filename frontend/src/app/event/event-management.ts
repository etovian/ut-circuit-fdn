import {Component, inject, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {EventService} from './event.service';
import {EventTemplate, ScheduledEventInstance} from './event.model';
import {CongregationService} from '../congregation/congregation.service';
import {Congregation} from '../congregation/congregation.model';
import {FormsModule} from '@angular/forms';
import {switchMap, tap} from 'rxjs';

@Component({
  selector: 'app-event-management',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './event-management.html',
  styleUrl: './event-management.css'
})
export class EventManagement {
  public route = inject(ActivatedRoute);
  private eventService = inject(EventService);
  private congregationService = inject(CongregationService);

  congregation = signal<Congregation | undefined>(undefined);
  templates = signal<EventTemplate[]>([]);
  instances = signal<ScheduledEventInstance[]>([]);
  
  // UI State
  viewMode = signal<'templates' | 'calendar'>('calendar'); // Default to calendar
  isEditingTemplate = signal(false);
  isEditingInstance = signal(false);
  currentTemplate = signal<Partial<EventTemplate>>({});
  currentInstance = signal<Partial<ScheduledEventInstance>>({});

  // End Condition State
  endConditionType = signal<'never' | 'date' | 'count'>('never');
  occurrenceCount = signal<number>(1);

  // Calendar State
  currentMonth = signal(new Date());
  calendarDays = signal<any[]>([]);

  daysOfWeek = [
    { label: 'Sun', value: 'SU' },
    { label: 'Mon', value: 'MO' },
    { label: 'Tue', value: 'TU' },
    { label: 'Wed', value: 'WE' },
    { label: 'Thu', value: 'TH' },
    { label: 'Fri', value: 'FR' },
    { label: 'Sat', value: 'SA' }
  ];

  selectedDays = signal<string[]>([]);

  constructor() {
    this.route.params.pipe(
      switchMap(params => this.congregationService.getCongregationBySlug(params['slug'])),
      tap(church => {
        this.congregation.set(church);
        if (church && church.id) {
          this.loadData(church.id);
        }
      })
    ).subscribe();
  }

  loadData(congregationId: number) {
    this.eventService.getTemplates(congregationId).subscribe(data => {
      this.templates.set(data);
    });

    this.loadCalendarInstances(congregationId);
  }

  loadCalendarInstances(congregationId: number) {
    const month = this.currentMonth();
    const start = new Date(month.getFullYear(), month.getMonth(), 1);
    const end = new Date(month.getFullYear(), month.getMonth() + 1, 0, 23, 59, 59);
    
    // Expand range to cover full weeks
    start.setDate(start.getDate() - start.getDay());
    end.setDate(end.getDate() + (6 - end.getDay()));

    this.eventService.getScheduledInstances(congregationId, start.toISOString(), end.toISOString()).subscribe(data => {
      this.instances.set(data);
      this.generateCalendar();
    });
  }

  generateCalendar() {
    const month = this.currentMonth();
    const start = new Date(month.getFullYear(), month.getMonth(), 1);
    const end = new Date(month.getFullYear(), month.getMonth() + 1, 0);
    
    const firstDayOfWeek = start.getDay();
    const days: any[] = [];

    // Padding for previous month
    const prevMonthEnd = new Date(month.getFullYear(), month.getMonth(), 0);
    for (let i = firstDayOfWeek - 1; i >= 0; i--) {
      days.push({
        date: new Date(month.getFullYear(), month.getMonth() - 1, prevMonthEnd.getDate() - i),
        isPadding: true,
        events: []
      });
    }

    // Days of current month
    for (let i = 1; i <= end.getDate(); i++) {
      const date = new Date(month.getFullYear(), month.getMonth(), i);
      days.push({
        date,
        isPadding: false,
        isToday: this.isSameDay(date, new Date()),
        events: this.getEventsForDate(date)
      });
    }

    // Padding for next month
    const remaining = 42 - days.length;
    for (let i = 1; i <= remaining; i++) {
      days.push({
        date: new Date(month.getFullYear(), month.getMonth() + 1, i),
        isPadding: true,
        events: []
      });
    }

    this.calendarDays.set(days);
  }

  private isSameDay(d1: Date, d2: Date) {
    return d1.getFullYear() === d2.getFullYear() &&
           d1.getMonth() === d2.getMonth() &&
           d1.getDate() === d2.getDate();
  }

  private getEventsForDate(date: Date) {
    return this.instances().filter(i => {
      const eventDate = new Date(i.startTime);
      return this.isSameDay(date, eventDate);
    }).sort((a, b) => a.startTime.localeCompare(b.startTime));
  }

  changeMonth(delta: number) {
    const next = new Date(this.currentMonth());
    next.setMonth(next.getMonth() + delta);
    this.currentMonth.set(next);
    if (this.congregation()?.id) {
      this.loadCalendarInstances(this.congregation()!.id!);
    }
  }

  editTemplate(template?: EventTemplate, prefillDate?: Date) {
    if (template) {
      this.currentTemplate.set({ ...template });
      const rrule = template.recurrenceRule;
      
      // Parse Days
      const daysMatch = rrule.match(/BYDAY=([^;]+)/);
      if (daysMatch) {
        this.selectedDays.set(daysMatch[1].split(','));
      } else {
        this.selectedDays.set([]);
      }

      // Parse End Condition
      const countMatch = rrule.match(/COUNT=(\d+)/);
      if (template.endDate) {
        this.endConditionType.set('date');
      } else if (countMatch) {
        this.endConditionType.set('count');
        this.occurrenceCount.set(parseInt(countMatch[1], 10));
      } else {
        this.endConditionType.set('never');
      }
    } else {
      const baseDate = prefillDate || new Date();
      const year = baseDate.getFullYear();
      const month = String(baseDate.getMonth() + 1).padStart(2, '0');
      const day = String(baseDate.getDate()).padStart(2, '0');
      const dateStr = `${year}-${month}-${day}`;

      const dayNames = ['SU', 'MO', 'TU', 'WE', 'TH', 'FR', 'SA'];
      const dayOfWeek = dayNames[baseDate.getDay()];

      this.currentTemplate.set({
        congregationId: this.congregation()?.id,
        startDate: dateStr,
        startTime: '10:00:00',
        durationMinutes: 60,
        recurrenceRule: `FREQ=WEEKLY;BYDAY=${dayOfWeek}`,
        isActive: true
      });
      this.selectedDays.set([dayOfWeek]);
      
      if (prefillDate) {
        this.endConditionType.set('count');
        this.occurrenceCount.set(1);
      } else {
        this.endConditionType.set('never');
      }
    }
    this.isEditingTemplate.set(true);
  }

  toggleDay(day: string) {
    this.selectedDays.update(days => 
      days.includes(day) ? days.filter(d => d !== day) : [...days, day]
    );
  }

  saveTemplate() {
    const t = this.currentTemplate() as EventTemplate;
    
    // 1. Build Base RRULE
    let rrule = `FREQ=WEEKLY;BYDAY=${this.selectedDays().join(',')}`;

    // 2. Handle End Condition
    if (this.endConditionType() === 'count') {
      rrule += `;COUNT=${this.occurrenceCount()}`;
      t.endDate = undefined;
    } else if (this.endConditionType() === 'date') {
      // Keep t.endDate as set by the input
    } else {
      t.endDate = undefined;
    }
    
    t.recurrenceRule = rrule;

    const obs = t.id 
      ? this.eventService.updateTemplate(t.id, t)
      : this.eventService.createTemplate(t);

    obs.subscribe(() => {
      this.isEditingTemplate.set(false);
      if (this.congregation()?.id) {
        this.loadData(this.congregation()!.id!);
      }
    });
  }

  editInstance(instance: ScheduledEventInstance) {
    this.currentInstance.set({ ...instance });
    this.isEditingInstance.set(true);
  }

  saveInstanceOverride() {
    const i = this.currentInstance() as ScheduledEventInstance;
    if (i.id) {
      this.eventService.overrideInstance(i.id, i).subscribe(() => {
        this.isEditingInstance.set(false);
        if (this.congregation()?.id) {
          this.loadData(this.congregation()!.id!);
        }
      });
    }
  }

  deleteTemplate(id: number) {
    if (confirm('Are you sure you want to deactivate this event template?')) {
      this.eventService.deleteTemplate(id).subscribe(() => {
        if (this.congregation()?.id) {
          this.loadData(this.congregation()!.id!);
        }
      });
    }
  }

  cancelInstance(id: number) {
    if (confirm('Cancel this specific event occurrence?')) {
      this.eventService.cancelInstance(id).subscribe(() => {
        if (this.congregation()?.id) {
          this.loadData(this.congregation()!.id!);
        }
      });
    }
  }

  cancelSeriesFrom(templateId: number, startFrom: string) {
    if (confirm('Are you sure you want to cancel this and ALL future occurrences in this series?')) {
      this.eventService.cancelSeriesFrom(templateId, startFrom).subscribe(() => {
        this.isEditingInstance.set(false);
        if (this.congregation()?.id) {
          this.loadData(this.congregation()!.id!);
        }
      });
    }
  }
}
