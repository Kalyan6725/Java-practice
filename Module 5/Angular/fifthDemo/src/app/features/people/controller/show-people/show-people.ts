import { Component, inject } from '@angular/core';
import { PeopleService } from '../../service/people-service';

@Component({
  selector: 'app-show-people',
  imports: [],
  templateUrl: './show-people.html',
  styleUrl: './show-people.css',
})
export class ShowPeople {
  peopleService:PeopleService=inject(PeopleService);
}
