import { Component, Input, Output, EventEmitter } from '@angular/core';
import { PersonDTO } from '../../../types/PersonDTO';

@Component({
  selector: 'app-person',
  imports: [],
  templateUrl: './person.html',
  styleUrl: './person.css',
})
export class Person {
  @Input()
  public p!:PersonDTO;

  @Output()
  public removeEvent = new EventEmitter<number>();

  @Output()
  public updateEvent = new EventEmitter<PersonDTO>();

  update(person: PersonDTO) {
    console.log(`Update person:`, person);
    this.updateEvent.emit(person);
  }

  remove(id: number) {
    //alert(`Remove person with ID: ${id}`);
    console.log(`Remove person with ID: ${id}`);
    this.removeEvent.emit(id);
  }
}
