import {Component, computed, inject, Input, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {MarkdownComponent} from 'ngx-markdown';
import {LMarkdownEditorModule} from 'ngx-markdown-editor';
import {Congregation} from './congregation.model';
import {CongregationService} from './congregation.service';
import {ToastService} from '../shared/toast.service';
import {marked} from 'marked';

import 'ace-builds/src-noconflict/ace';
import 'ace-builds/src-noconflict/mode-markdown';
import 'ace-builds/src-noconflict/theme-eclipse';

// Satisfy ngx-markdown-editor's requirement for global marked as a function
if (typeof window !== 'undefined') {
  (window as any).marked = marked;
}


@Component({
  selector: 'app-congregation-about',
  standalone: true,
  imports: [CommonModule, FormsModule, MarkdownComponent, LMarkdownEditorModule],
  templateUrl: './congregation-about.html',
  styleUrl: './congregation-about.css'
})
export class CongregationAbout {
  @Input({ required: true }) set church(value: Congregation | undefined) {
    this._church.set(value);
  }
  get church() { return this._church(); }

  private _church = signal<Congregation | undefined>(undefined);
  private congregationService = inject(CongregationService);
  private toastService = inject(ToastService);

  isEditingDescription = signal(false);
  editingDescriptionText = signal('');

  googleMapsUrl = computed(() => {
    const church = this._church();
    if (!church) return undefined;

    const physicalAddress = church.addresses?.find(a => a.addressType === 'PHYSICAL');
    if (!physicalAddress) return undefined;

    const addr = physicalAddress.address;
    const query = `${church.name} ${addr.streetAddress} ${addr.city} ${addr.state} ${addr.zipCode}`;
    return `https://www.google.com/maps/search/?api=1&query=${encodeURIComponent(query)}`;
  });

  openEditDescription() {
    const church = this._church();
    if (church) {
      this.editingDescriptionText.set(church.description || '');
      this.isEditingDescription.set(true);
    }
  }

  closeEditDescription() {
    this.isEditingDescription.set(false);
  }

  saveDescription() {
    const church = this._church();
    const newDescription = this.editingDescriptionText();

    if (church && church.id) {
      // Optimistic update
      this._church.update(c => c ? { ...c, description: newDescription } : undefined);
      this.isEditingDescription.set(false);

      this.congregationService.updateDescription(church.id, newDescription)
        .subscribe({
          next: (updated) => {
            this._church.set(updated);
            this.toastService.success('Description updated successfully');
          },
          error: (err) => {
            console.error('Failed to update description', err);
            this._church.set(church);
            this.toastService.error('Failed to update description');
          }
        });
    }
  }
}
