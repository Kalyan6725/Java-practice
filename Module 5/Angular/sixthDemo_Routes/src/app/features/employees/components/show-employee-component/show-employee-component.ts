import { Component, inject, Input } from '@angular/core';
import { EmployeeDTO } from '../../../../dto/EmployeeDTO';
import { EmployeesService } from '../../service/employees-service';

@Component({
  selector: 'app-show-employee-component',
  imports: [],
  templateUrl: './show-employee-component.html',
  styleUrl: './show-employee-component.css',
})
export class ShowEmployeeComponent {
  employeesService: EmployeesService = inject(EmployeesService);
  @Input()
  employee:EmployeeDTO = {id:0,name:'',email:''};
  status: {loading:boolean,error:string,success:boolean} = {loading:false,error:'',success:false};
  DeleteEmp(id:number) {
    if (!confirm('Are you sure you want to delete this employee?')) {
      return;
    }
    this.status.loading = true;
    console.log('Delete employee with id:', id);
    console.log('Current employees before delete:', this.employeesService.employeesList());
    this.employeesService.deleteEmployee(id).subscribe({
      next: (response) => {
        console.log('Delete response:', response);
        console.log('Employee deleted successfully');
        const updatedEmployees = this.employeesService.employeesList().filter(emp => emp.id !== id);
        console.log('Updated employees after filter:', updatedEmployees);
        this.employeesService.employeesList.set(updatedEmployees);
        this.status.loading = false;
        this.status.success = true;
      },
      error: (error) => {
        console.error('Error deleting employee:', error);
        this.status.loading = false;
        this.status.error = 'Error deleting employee';
      }
    });
  }
}
