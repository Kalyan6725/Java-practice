import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { EmployeesService } from '../../service/employees-service';
import { EmployeeReqDTO } from '../../../../dto/EmployeeReqDTO';

@Component({
  selector: 'app-add-employee',
  imports: [FormsModule],
  templateUrl: './add-employee.html',
  styleUrl: './add-employee.css',
})
export class AddEmployee {
  employee :EmployeeReqDTO = {name:'',email:''};

  employeesService: EmployeesService = inject(EmployeesService);
  addEmployee() {
    console.log('Adding employee:', this.employee);
    console.log('Current employees before add:', this.employeesService.employeesList());
    this.employeesService.addEmployee(this.employee).subscribe({
      next: (response) => {
        console.log('Employee added successfully:', response);
        const updatedEmployees = [...this.employeesService.employeesList(), response];
        console.log('Updated employees after add:', updatedEmployees);
        this.employeesService.employeesList.set(updatedEmployees);
        this.employee = {name:'',email:''}; // Reset form
      },
      error: (error) => {
        console.error('Error adding employee:', error);
      }
    });

  }
}
