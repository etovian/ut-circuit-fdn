export interface WorshipTime {
  title: string;
  time: string;
  description: string;
}

export interface Address {
  id: number;
  streetAddress: string;
  addressLine2?: string;
  city: string;
  state: string;
  zipCode: string;
}

export interface CongregationAddress {
  address: Address;
  addressType: 'PHYSICAL' | 'MAILING';
}

export interface PersonRelation {
  id: number;
  firstName: string;
  lastName: string;
  position: string;
  photoUrl?: string;
}

export interface Congregation {
  id?: number;
  name: string;
  description: string;
  mission: string;
  bannerPhotos?: string[];
  addresses?: CongregationAddress[];
  tagline?: string;
  location?: string;
  worshipTimes?: WorshipTime[];
  persons?: PersonRelation[];
}

export interface ScheduledEvent {
  id: number;
  templateId: number;
  startTime: string;
  originalStartTime: string;
  durationMinutes: number;
  name: string;
  description: string;
  location: string;
  cancelled: boolean;
  override: boolean;
}
