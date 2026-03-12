export enum ExternalLinkType {
  WEBSITE = 'WEBSITE',
  FACEBOOK = 'FACEBOOK',
  YOUTUBE = 'YOUTUBE',
  SUBSTACK = 'SUBSTACK'
}

export interface CongregationExternalLink {
  id?: number;
  congregationId: number;
  title: string;
  description?: string;
  externalLinkType: ExternalLinkType;
  url: string;
  ordinalValue: number;
}
