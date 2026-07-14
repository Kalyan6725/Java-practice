import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { EmployeesService } from '../../service/employees-service';
import { EmployeeDTO } from '../../../../dto/EmployeeDTO';
import { ShowEmployeeComponent } from '../show-employee-component/show-employee-component';
import { AddEmployee } from '../add-employee/add-employee';

@Component({
  selector: 'app-show-employees-component',
  imports: [ShowEmployeeComponent,AddEmployee],
  templateUrl: './show-employees-component.html',
  styleUrl: './show-employees-component.css',
})
export class ShowEmployeesComponent implements OnInit {

  ngOnInit(): void {
    this.status.set({loading:true,error:'',success:false});
    setTimeout(() => {
      this.getAllEmployees();
    }, 2000);
    
  }
  
  status:WritableSignal<{loading:boolean,error:string,success:boolean}> = signal({loading:true,error:'',success:false});
  employeesService: EmployeesService = inject(EmployeesService);

  getAllEmployees() {
    this.employeesService.getEmployees().subscribe({
      next: (employees) => {
        console.log('Employees:', employees);
        this.employeesService.employeesList.set(employees);
        this.status.set({loading:false,error:'',success:true});
      },
      error: (error) => {
        console.error('Error loading employees:', error);
        this.status.set({loading:false,error:'Failed to load employees',success:false});
      }
    });
  }
}
