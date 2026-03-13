export enum ExternalLinkType {
  FACEBOOK = 'FACEBOOK',
  INSTAGRAM = 'INSTAGRAM',
  SUBSTACK = 'SUBSTACK',
  WEBSITE = 'WEBSITE',
  YOUTUBE = 'YOUTUBE'
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
