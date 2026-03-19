export interface PersonContactInfo {
  id?: number;
  personId?: number;
  contactInfoType: 'PHONE_NUMBER' | 'EMAIL';
  contactValue: string;
}

export interface Person {
  id?: number;
  title?: string;
  firstName: string;
  middleName?: string;
  lastName: string;
  suffix?: string;
  dateOfBirth?: string;
  photoFileName?: string;
  biography?: string;
  congregations?: CongregationRelation[];
  contactInfos?: PersonContactInfo[];
}

export interface CongregationRelation {
  id: number;
  name: string;
  position: string;
}

export interface CongregationPerson {
  person: Person;
  position: string;
}

export interface PersonRelation {
  id: number;
  firstName: string;
  lastName: string;
  position: string;
  photoUrl?: string;
  biography?: string;
  sortOrdinalValue?: number;
  contactInfos?: PersonContactInfo[];
}
