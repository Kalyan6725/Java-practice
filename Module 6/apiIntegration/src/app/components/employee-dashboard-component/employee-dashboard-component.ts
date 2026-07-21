import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import EmployeeDTO from '../../dto/EmployeeDTO';
import { EmployeeDetailModal } from "../employee-detail-modal/employee-detail-modal";
import { NavbarComponent } from "../navbar-component/navbar-component";
import { EmployeeService } from '../../services/employee-service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-employee-dashboard-component',
  imports: [EmployeeDetailModal, RouterModule],
  templateUrl: './employee-dashboard-component.html',
  styleUrl: './employee-dashboard-component.css',
})
export class EmployeeDashboardComponent implements OnInit {
employeeService: EmployeeService = inject(EmployeeService);
pstatus: WritableSignal<{ loading: boolean; error?: string | null; success?: boolean }> = signal({
    loading: false,
  });
  selectedEmployee: EmployeeDTO | null = null;


  viewEmployee(employee: EmployeeDTO): void {
    this.selectedEmployee = employee;
  }

  deleteEmployee(employee: EmployeeDTO): void {
    if (confirm(`Are you sure you want to delete ${employee.name}? (Triggers: DELETE /employees/deleteById/${employee.id})`)) {
      this.pstatus.set({ loading: true });
      if (employee.id !== undefined) {
        this.employeeService.remove(employee.id).subscribe({
          next: (response) => {
            console.log('Employee deleted successfully', response);
            this.employeeService.employees.set(this.employeeService.employees().filter(e => e.id !== employee.id));
            this.pstatus.set({ loading: false, success: true });
          },
          error: (error) => {
            console.error('Error deleting employee:', error);
            console.error('Error status:', error.status);
            console.error('Error details:', error.error);
            this.pstatus.set({ loading: false, error: 'Failed to delete employee.' });
          },
          complete: () => {
            console.log('Delete operation completed');
          }
        });
      }
    }
  }
  ngOnInit() {
    this.pstatus.set({loading: true});
    setTimeout(() => {
    this.getAllEmployees();
    }, 1000);
  }
  getAllEmployees() {
    this.employeeService.getAll().subscribe({
      next: (data) => {
        this.pstatus.set({loading: false, success: true});
        console.log(data);
        this.employeeService.employees.set(data);
      },
      error: (error) => {
        console.error('Error fetching employee details:', error);
        this.pstatus.set({loading: false, error: 'Failed to fetch employee details.'});
      },
      complete: () => {
        console.log('Employee details fetched successfully.');
      },
    });
  }
}
