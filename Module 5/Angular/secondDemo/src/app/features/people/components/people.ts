import { Component, OnInit } from '@angular/core';
import { Person } from '../../person/components/person';
import { PersonDTO } from '../../../types/PersonDTO';
import { AddPerson } from '../../add-person/add-person';

@Component({
  selector: 'app-people',
  imports: [Person, AddPerson],
  templateUrl: './people.html',
  styleUrl: './people.css',
})
export class People implements OnInit {
  private readonly storageKey = 'people-list';

  private readonly defaultPeople: PersonDTO[] = [
    { id: 1, name: 'Alice Johnson', age: 28, email: 'alice@example.com' },
    { id: 2, name: 'Ravi Kumar', age: 32, email: 'ravi@example.com' },
    { id: 3, name: 'Maria Chen', age: 24, email: 'maria@example.com' },
  ];

  public people: PersonDTO[] = [];
  public selectedPerson: PersonDTO | null = null;

  ngOnInit() {
    this.people = this.loadOrInitializePeople();
  }

  private loadOrInitializePeople(): PersonDTO[] {
    if (typeof localStorage === 'undefined') {
      return [...this.defaultPeople];
    }

    const saved = localStorage.getItem(this.storageKey);

    if (saved) {
      try {
        return JSON.parse(saved) as PersonDTO[];
      } catch {
        return [...this.defaultPeople];
      }
    }

    localStorage.setItem(this.storageKey, JSON.stringify(this.defaultPeople));
    return [...this.defaultPeople];
  }

  private savePeople() {
    if (typeof localStorage === 'undefined') {
      return;
    }

    localStorage.setItem(this.storageKey, JSON.stringify(this.people));
  }

  removePerson(id: number) {
    this.people = this.people.filter(person => person.id !== id);
    this.savePeople();
  }
  addPerson(person: PersonDTO) {
    this.people.push(person);
    this.savePeople();
  }

  handleUpdateClick(person: PersonDTO) {
    console.log('Selected person for update:', person);
    this.selectedPerson = person;
  }

  updatePersonInArray(updatedPerson: PersonDTO) {
    const index = this.people.findIndex(p => p.id === updatedPerson.id);
    if (index !== -1) {
      this.people[index] = updatedPerson;
      this.savePeople();
      this.selectedPerson = null;
    }
  }

  changeTitle() {
    console.log('change title clicked');
    var h1Elements = document.getElementsByTagName('h1');
    if (h1Elements.length > 0) {
      h1Elements[0].textContent = 'Title Changed!';
    }
  }
}
