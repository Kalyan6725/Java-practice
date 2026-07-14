import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import EmployeeDTO from './dto/EmployeeDTO';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  ngOnInit() {
    this.getAll();
  }

protected httpClient:HttpClient=inject(HttpClient);
  changeTitle() {
    this.title = 'Angular 17';
  }
  protected employeeDtos: WritableSignal<EmployeeDTO[]> = signal<EmployeeDTO[]>([]);
  protected title = 'firstDemo' ;
  protected person = {fname: 'Kalyan', lname: 'Samineni'};
  newName = '';
  names = ['Kalyan', 'Samineni', 'Ravi', 'Ramesh'];

  employees = signal([
    {id: 1, name: 'Kalyan', age: 30},
    {id: 2, name: 'Samineni', age: 25},
    {id: 3, name: 'Ravi', age: 35},
  ]);
  editEmployee(empId: number) {
    const updatedEmployees = this.employees().map(emp => {
      if (emp.id === empId) {
        const newName = prompt(`Editing Employee: ${emp.name}`);
        return { ...emp, name: newName || emp.name }; // Update the name or any other property
      }
      return emp;
    });
    this.employees.set(updatedEmployees);
  }

  deleteEmployee(empId: number) {
    const updatedEmployees = this.employees().filter(emp => emp.id !== empId);
    this.employees.set(updatedEmployees);
  }
  addName() {
    if (this.newName.trim()) {
      this.names.push(this.newName.trim());
      this.newName = '';
    }
  }
  updateName(index: number) {
    if (this.newName.trim()) {
      this.names[index] = this.newName.trim();
      this.newName = '';
    }
  }
  getAll(){
    this.httpClient.get<EmployeeDTO[]>("http://localhost:8080/employees/getAll").subscribe((data) => {
      console.log(data);
      this.employeeDtos.set(data);

    });
  }
}
