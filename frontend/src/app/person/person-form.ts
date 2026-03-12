import {Component, input, output} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Person} from './person.model';

@Component({
  selector: 'app-person-form',
  standalone: true,
  imports: [FormsModule],
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

  onSave() {
    this.save.emit(this.person());
  }

  onCancel() {
    this.cancel.emit();
  }
}
