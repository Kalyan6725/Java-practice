import { Component, inject, Input, signal, WritableSignal } from '@angular/core';
import EmployeeDTO from '../../dto/EmployeeDTO';
import { EmployeeService } from '../../services/employee-service';

@Component({
  selector: 'app-show-employee',
  imports: [],
  templateUrl: './show-employee.html',
  styleUrl: './show-employee.css',
})
export class ShowEmployee {
  employeeService: EmployeeService = inject(EmployeeService);
  @Input() employee!: EmployeeDTO;
  protected status: WritableSignal<{ loading: boolean; error?: string | null; success?: boolean }> = signal({
    loading: false,
  });
  remove() {
    this.status.set({ loading: true });
    if (this.employee.id !== undefined) {
      this.employeeService.remove(this.employee.id).subscribe({
        next: (response) => {
          console.log('Employee removed successfully.', response);
          this.employeeService.employees.set(this.employeeService.employees().filter((emp) => emp.id !== this.employee.id));
          this.status.set({ loading: false, success: true});
        },
        error: (error) => {
          console.error('Error removing employee:', error);
          console.error('Error status:', error.status);
          console.error('Error details:', error.error);
          this.status.set({ loading: false,success: false,error: 'Failed to remove employee.' });
        },
        complete: () => {
          console.log('Remove operation completed');
        }
      });
    }
  }
}
