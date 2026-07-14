import { Component, Output, EventEmitter, Input } from '@angular/core';
import { PersonDTO } from '../../types/PersonDTO';

@Component({
  selector: 'app-add-person',
  imports: [],
  templateUrl: './add-person.html',
  styleUrl: './add-person.css',
})
export class AddPerson {
  @Input()
  public selectedPerson: PersonDTO | null = null;

  @Output()
  public addPersonEvent = new EventEmitter<PersonDTO>();

  @Output()
  public updatePersonEvent = new EventEmitter<PersonDTO>();

  addPerson(idInput: HTMLInputElement, nameInput: HTMLInputElement, ageInput: HTMLInputElement, emailInput: HTMLInputElement) {
    const id = parseInt(idInput.value);
    const name = nameInput.value.trim();
    const age = parseInt(ageInput.value);
    const email = emailInput.value.trim();

    if (!id || !name || !age || !email || isNaN(id) || isNaN(age)) {
      alert('Please fill all fields correctly');
      return;
    }

    const personDTO: PersonDTO = { id, name, age, email };
    this.addPersonEvent.emit(personDTO);

    // Clear inputs after adding
    idInput.value = '';
    nameInput.value = '';
    ageInput.value = '';
    emailInput.value = '';
  }

  updatePerson(idInput: HTMLInputElement, nameInput: HTMLInputElement, ageInput: HTMLInputElement, emailInput: HTMLInputElement) {
    const id = parseInt(idInput.value);
    const name = nameInput.value.trim();
    const age = parseInt(ageInput.value);
    const email = emailInput.value.trim();

    if (!id || !name || !age || !email || isNaN(id) || isNaN(age)) {
      alert('Please fill all fields correctly');
      return;
    }

    const personDTO: PersonDTO = { id, name, age, email };
    this.updatePersonEvent.emit(personDTO);

    // Clear inputs after updating
    idInput.value = '';
    nameInput.value = '';
    ageInput.value = '';
    emailInput.value = '';
  }
}
