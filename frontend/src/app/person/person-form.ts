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

  save = output<Person>();
  cancel = output<void>();

  // Local state for the form
  formData = signal<Person>({ firstName: '', lastName: '' });

  constructor() {
    // Update local state when input changes
    effect(() => {
      const p = this.person();
      this.formData.set({ 
        ...p,
        contactInfos: p.contactInfos ? [...p.contactInfos.map(ci => ({...ci}))] : []
      });
    });
  }

  updateField(field: keyof Person, value: any) {
    this.formData.update(data => ({
      ...data,
      [field]: value
    }));
  }

  onSave() {
    this.save.emit(this.formData());
  }

  onCancel() {
    this.cancel.emit();
  }
}
