export interface WorshipTime {
  title: string;
  time: string;
  description: string;
}

export interface Congregation {
  id?: number;
  name: string;
  description: string;
  mission: string;
  tagline?: string;
  location?: string;
  address?: string;
  googleMapsLink?: string;
  worshipTimes?: WorshipTime[];
}
