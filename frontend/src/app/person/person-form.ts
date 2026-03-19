import {Component, effect, input, output, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Person} from './person.model';
import {PersonContactInfoAdmin} from './person-contact-info-admin';

@Component({
  selector: 'app-person-form',
  standalone: true,
  imports: [FormsModule, PersonContactInfoAdmin],
  templateUrl: './person-form.html',
  styleUrl: './person-form.css'
})
export class PersonForm {
  person = input<Person>({
    firstName: '',
    lastName: ''
  });
  position = input<string>('');
  congregationName = input<string>('');

  save = output<{ person: Person, position: string }>();
  cancel = output<void>();

  // Local state for the form
  formData = signal<Person>({ firstName: '', lastName: '' });
  formDataPosition = signal<string>('');
  activeTab = signal<'name-position' | 'biographical' | 'contact'>('name-position');

  constructor() {
    // Update local state when input changes
    effect(() => {
      const p = this.person();
      this.formData.set({ 
        ...p,
        contactInfos: p.contactInfos ? [...p.contactInfos.map(ci => ({...ci}))] : []
      });
    });

    effect(() => {
      this.formDataPosition.set(this.position());
    });
  }

  updateField(field: keyof Person, value: any) {
    this.formData.update(data => ({
      ...data,
      [field]: value
    }));
  }

  updatePosition(value: string) {
    this.formDataPosition.set(value);
  }

  onSave() {
    this.save.emit({
      person: this.formData(),
      position: this.formDataPosition()
    });
  }

  onCancel() {
    this.cancel.emit();
  }

  setActiveTab(tab: 'name-position' | 'biographical' | 'contact') {
    this.activeTab.set(tab);
  }
}
