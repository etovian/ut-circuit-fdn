import {Component, input} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {PersonContactInfo} from './person.model';

@Component({
  selector: 'app-person-contact-info-admin',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './person-contact-info-admin.html',
  styleUrl: './person-contact-info-admin.css'
})
export class PersonContactInfoAdmin {
  contactInfos = input.required<PersonContactInfo[]>();
  
  addContact() {
    this.contactInfos().push({
      contactInfoType: 'EMAIL',
      contactValue: ''
    });
  }

  removeContact(info: PersonContactInfo) {
    const index = this.contactInfos().indexOf(info);
    if (index >= 0) {
      this.contactInfos().splice(index, 1);
    }
  }
}
