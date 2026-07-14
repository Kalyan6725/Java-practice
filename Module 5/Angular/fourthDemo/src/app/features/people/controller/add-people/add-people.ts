import { Component, inject } from '@angular/core';
import { PeopleService } from '../../service/people-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-people',
  imports: [FormsModule],
  templateUrl: './add-people.html',
  styleUrl: './add-people.css',
})
export class AddPeople {
  peopleService: PeopleService = inject(PeopleService);
  name: string = '';
  addName(): void {
    if (this.name.trim()!== '') {
      this.peopleService.addName(this.name.trim());
      this.name = '';
    }
  }
}
