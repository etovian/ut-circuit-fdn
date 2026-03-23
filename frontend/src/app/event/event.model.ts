export interface EventTemplate {
  id?: number;
  name: string;
  description: string;
  location?: string;
  congregationId: number;
  startDate: string; // ISO date string
  endDate?: string;  // ISO date string
  startTime: string; // HH:mm:ss string
  durationMinutes: number;
  recurrenceRule: string; // RFC 5545 RRULE
  isActive: boolean;
  isCircuitEvent: boolean;
}

export interface ScheduledEventInstance {
  id?: number;
  templateId: number;
  startTime: string; // ISO date-time string
  originalStartTime: string; // ISO date-time string
  durationMinutes: number;
  name: string;
  description: string;
  location?: string;
  isCancelled: boolean;
  isOverride: boolean;
  isCircuitEvent: boolean;
}
