import { Component, inject } from '@angular/core';
import { PeopleService } from '../../service/people-service';
import { AsyncPipe } from '@angular/common';

@Component({
  selector: 'app-show-people',
  imports: [AsyncPipe],
  templateUrl: './show-people.html',
  styleUrl: './show-people.css',
})
export class ShowPeople {
  peopleService:PeopleService=inject(PeopleService);
}
