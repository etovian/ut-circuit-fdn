import {Component, inject, OnInit, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {CongregationService} from './congregation.service';
import {ExternalLinkService} from './external-link.service';
import {Congregation} from './congregation.model';
import {CongregationExternalLink, ExternalLinkType} from './external-link.model';
import {ToastService} from '../shared/toast.service';

@Component({
  selector: 'app-congregation-external-link-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './congregation-external-link-admin.html',
  styleUrl: './congregation-external-link-admin.css'
})
export class CongregationExternalLinkAdmin implements OnInit {
  private route = inject(ActivatedRoute);
  private congregationService = inject(CongregationService);
  private externalLinkService = inject(ExternalLinkService);
  private toastService = inject(ToastService);

  congregation = signal<Congregation | undefined>(undefined);
  links = signal<CongregationExternalLink[]>([]);
  isModalOpen = signal<boolean>(false);
  editingLink = signal<CongregationExternalLink | null>(null);

  // Form fields
  linkTitle = '';
  linkDescription = '';
  linkUrl = '';
  linkType = ExternalLinkType.WEBSITE;

  ExternalLinkType = ExternalLinkType;
  linkTypes = Object.values(ExternalLinkType);

  ngOnInit() {
    const slug = this.route.snapshot.paramMap.get('slug');
    if (slug) {
      this.congregationService.getCongregationBySlug(slug).subscribe(c => {
        this.congregation.set(c);
        if (c && c.id) {
          this.loadLinks(c.id);
        }
      });
    }
  }

  loadLinks(congregationId: number) {
    this.externalLinkService.getLinks(congregationId).subscribe(links => {
      this.links.set(links);
    });
  }

  openCreateModal() {
    this.editingLink.set(null);
    this.linkTitle = '';
    this.linkDescription = '';
    this.linkUrl = '';
    this.linkType = ExternalLinkType.WEBSITE;
    this.isModalOpen.set(true);
  }

  openEditModal(link: CongregationExternalLink) {
    this.editingLink.set(link);
    this.linkTitle = link.title;
    this.linkDescription = link.description || '';
    this.linkUrl = link.url;
    this.linkType = link.externalLinkType;
    this.isModalOpen.set(true);
  }

  closeModal() {
    this.isModalOpen.set(false);
  }

  saveLink() {
    const c = this.congregation();
    if (!c || !c.id) return;

    const linkData: CongregationExternalLink = {
      id: this.editingLink()?.id,
      congregationId: c.id,
      title: this.linkTitle,
      description: this.linkDescription,
      url: this.linkUrl,
      externalLinkType: this.linkType,
      ordinalValue: this.editingLink()?.ordinalValue || 0
    };

    if (linkData.id) {
      this.externalLinkService.updateLink(c.id, linkData.id, linkData).subscribe({
        next: () => {
          this.toastService.success('Link updated successfully');
          this.loadLinks(c.id!);
          this.closeModal();
        },
        error: () => this.toastService.error('Failed to update link')
      });
    } else {
      this.externalLinkService.createLink(c.id, linkData).subscribe({
        next: () => {
          this.toastService.success('Link created successfully');
          this.loadLinks(c.id!);
          this.closeModal();
        },
        error: () => this.toastService.error('Failed to create link')
      });
    }
  }

  deleteLink(link: CongregationExternalLink) {
    const c = this.congregation();
    if (!c || !c.id || !link.id) return;

    if (confirm(`Are you sure you want to delete "${link.title}"?`)) {
      this.externalLinkService.deleteLink(c.id, link.id).subscribe({
        next: () => {
          this.toastService.success('Link deleted successfully');
          this.loadLinks(c.id!);
        },
        error: () => this.toastService.error('Failed to delete link')
      });
    }
  }

  moveUp(index: number) {
    if (index === 0) return;
    const list = [...this.links()];
    [list[index - 1], list[index]] = [list[index], list[index - 1]];
    this.updateOrder(list);
  }

  moveDown(index: number) {
    const list = [...this.links()];
    if (index === list.length - 1) return;
    [list[index], list[index + 1]] = [list[index + 1], list[index]];
    this.updateOrder(list);
  }

  private updateOrder(list: CongregationExternalLink[]) {
    const c = this.congregation();
    if (!c || !c.id) return;

    this.links.set(list);
    const linkIds = list.map(l => l.id!).filter(id => id !== undefined);
    this.externalLinkService.reorderLinks(c.id, linkIds).subscribe({
      error: () => this.toastService.error('Failed to update link order')
    });
  }

  getSlug(name: string): string {
    return name.toLowerCase().replace(/\s+/g, '-').replace(/[()]/g, '');
  }
}
