import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { NavbarComponent } from '../navbar-component/navbar-component';
import { EmployeeService } from '../../services/employee-service';
import { EmployeeReqDTO } from '../../dto/EmployeeReqDTO';

@Component({
  selector: 'app-employee-form',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './employee-form-component.html'
})
export class EmployeeFormComponent implements OnInit {
  employeeService: EmployeeService = inject(EmployeeService);
  status: WritableSignal<{ loading: boolean; error?: string | null; success?: boolean }> = signal({ loading: false });
  employeeForm!: FormGroup;
  isEditMode = false;
  employeeId: string | null = null;
  formSubmitted = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initForm();
    
    // Check if handling edit parameter payload via routing state
    this.employeeId = this.route.snapshot.paramMap.get('id');
    if (this.employeeId) {
      this.isEditMode = true;
      this.loadEmployeeMockData(this.employeeId);
    }
  }

  initForm(): void {
    this.employeeForm = this.fb.group({
      id: [{ value: '', disabled: true }],
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  get f() { return this.employeeForm.controls; }

  loadEmployeeMockData(id: string): void {
    // Load employee data from backend
    this.status.set({ loading: true });
    this.employeeService.getById(Number(id)).subscribe({
      next: (data) => {
        this.employeeForm.patchValue({
          id: data.id,
          name: data.name,
          email: data.email
        });
        this.status.set({ loading: false, success: true });
      },
      error: (error) => {
        console.error('Error loading employee:', error);
        this.status.set({ loading: false, error: 'Failed to load employee data.' });
      }
    });
  }

  onSubmit(): void {
    this.formSubmitted = true;

    if (this.employeeForm.invalid) {
      return;
    }

    const formValue = this.employeeForm.getRawValue();
    const payload: EmployeeReqDTO = {
      name: formValue.name,
      email: formValue.email
    };

    this.status.set({ loading: true });

    if (this.isEditMode) {
      console.log('Edit mode not yet implemented for PUT /employees/' + this.employeeId, payload);
      this.status.set({ loading: false, error: 'Edit functionality not implemented yet.' });
    } else {
      console.log('Dispatched Action: POST /employees/add', payload);
      this.employeeService.add(payload).subscribe({
        next: (data) => {
          console.log('Employee added successfully:', data);
          this.employeeService.employees.set([...this.employeeService.employees(), data]);
          this.status.set({ loading: false, success: true });
          // Redirect control back to view index following successful updates
          setTimeout(() => this.router.navigate(['/employees']), 1500);
        },
        error: (error) => {
          console.error('Error adding employee:', error);
          this.status.set({ loading: false, error: 'Failed to add employee.' });
        }
      });
    }
  }
}