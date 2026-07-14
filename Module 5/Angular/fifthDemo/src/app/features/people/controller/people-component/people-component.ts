import { Component } from '@angular/core';
import { ShowPeople } from '../show-people/show-people';
import { AddPeople } from '../add-people/add-people';
import { UpdatePeople } from '../update-people/update-people';

@Component({
  selector: 'app-people-component',
  imports: [ShowPeople, AddPeople, UpdatePeople],
  templateUrl: './people-component.html',
  styleUrl: './people-component.css',
})
export class PeopleComponent {}
