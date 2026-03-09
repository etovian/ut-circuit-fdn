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

export interface Congregation {
  id?: number;
  name: string;
  description: string;
  mission: string;
  addresses?: CongregationAddress[];
  tagline?: string;
  location?: string;
  address?: string; // Legacy field for hardcoded data, keep for now
  googleMapsLink?: string;
  worshipTimes?: WorshipTime[];
}
