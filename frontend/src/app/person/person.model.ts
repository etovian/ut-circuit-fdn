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
  sortOrdinalValue?: number;
}
