export interface Congregation {
  id?: number;
  name: string;
  description: string;
  mission: string;
  location?: string; // To be extracted from description if needed or added to DB
  slug?: string;    // For routing
}
