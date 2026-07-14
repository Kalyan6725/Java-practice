import { Component, inject } from '@angular/core';
import { PeopleService } from '../../service/people-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-update-people',
  imports: [FormsModule],
  templateUrl: './update-people.html',
  styleUrl: './update-people.css',
})
export class UpdatePeople {
  oldName: string = '';
  newName: string = '';
  peopleService: PeopleService = inject(PeopleService);

  updateName(): void {
    if (this.oldName && this.newName) {
      this.peopleService.updateName(this.oldName, this.newName);
      this.oldName = '';
      this.newName = '';
    }
  }
}
