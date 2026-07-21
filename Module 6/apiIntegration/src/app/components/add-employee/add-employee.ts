import { Component, inject, signal, WritableSignal } from '@angular/core';
import { EmployeeReqDTO } from '../../dto/EmployeeReqDTO';
import { FormsModule } from '@angular/forms';
import { EmployeeService } from '../../services/employee-service';

@Component({
  selector: 'app-add-employee',
  imports: [FormsModule],
  templateUrl: './add-employee.html',
  styleUrl: './add-employee.css',
})
export class AddEmployee {
  employeeService: EmployeeService = inject(EmployeeService);
  status:WritableSignal<{loading: boolean,error?: string,success?: boolean}> = signal({loading: false});
// Initialize our employee model using the DTO structure
  employee: EmployeeReqDTO = {
    name: '',
    email: ''
  };

  onSubmit(): void {
    this.status.set({loading: true});
    // Because of [(ngModel)], this.employee is already fully updated
    console.log('Employee Data Submitted:', this.employee);
    
    // TODO: Send this.employee to your backend service
    this.employeeService.add(this.employee).subscribe({
      next: (data) => {
        console.log('Employee added successfully:', data);
        this.employeeService.employees.set([...this.employeeService.employees(), data]);
        this.status.set({loading: false, success: true});
      },
      error: (error) => {
        console.error('Error adding employee:', error);
        this.status.set({loading: false, error: 'Failed to add employee'});
      }
    });
  }

  onReset(): void {
    this.employee = {
      name: '',
      email: ''
    };
  }
}
