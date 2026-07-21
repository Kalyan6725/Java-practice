import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CustomerService } from '../../../services/customer.service';
import { AdminCustomerRequest } from '../../../models/customer.model';
import { UserRole } from '../../../models/auth.model';

@Component({
  selector: 'app-customer-form',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <div class="page container">
      <div class="breadcrumb"><a routerLink="/admin/customers">Customers</a> / Add customer</div>
      <div class="page-head"><div><h1>Add customer</h1><p class="sub">Create a customer on behalf of an applicant.</p></div></div>

      @if (error()) { <div class="alert alert-warning">{{ error() }}</div> }

      <div class="card" style="max-width:720px"><div class="card-body">
        <form [formGroup]="form" (ngSubmit)="submit()">
          <div class="form-row">
            <div class="form-group"><label>Full name <span class="req">*</span></label><input class="input" formControlName="customerName" />
              @if (invalid('customerName')) { <div class="hint" style="color:var(--danger)">2–100 characters.</div> }
            </div>
            <div class="form-group"><label>Email <span class="req">*</span></label><input class="input" type="email" formControlName="email" />
              @if (invalid('email')) { <div class="hint" style="color:var(--danger)">Valid email required.</div> }
            </div>
          </div>
          <div class="form-row">
            <div class="form-group"><label>Phone <span class="req">*</span></label><input class="input" formControlName="phone" />
              @if (invalid('phone')) { <div class="hint" style="color:var(--danger)">10–15 digits.</div> }
            </div>
            <div class="form-group"><label>Password <span class="req">*</span></label><input class="input" type="password" formControlName="password" />
              @if (invalid('password')) { <div class="hint" style="color:var(--danger)">Minimum 6 characters.</div> }
            </div>
          </div>
          <div class="form-group"><label>Address <span class="req">*</span></label><input class="input" formControlName="address" />
            @if (invalid('address')) { <div class="hint" style="color:var(--danger)">5–255 characters.</div> }
          </div>
          <div class="form-row">
            <div class="form-group"><label>Branch <span class="req">*</span></label>
              <select class="select" formControlName="branch"><option>Bengaluru</option><option>Mumbai</option><option>Delhi</option><option>Chennai</option><option>Hyderabad</option></select>
            </div>
            <div class="form-group"><label>Role <span class="req">*</span></label>
              <select class="select" formControlName="role"><option>USER</option><option>UNDERWRITER</option><option>MANAGER</option><option>ADMIN</option></select>
            </div>
          </div>
          <div class="actions">
            <button type="submit" class="btn btn-primary btn-lg" [disabled]="saving()">{{ saving() ? 'Creating…' : 'Create customer' }}</button>
            <a routerLink="/admin/customers" class="btn btn-outline btn-lg">Cancel</a>
          </div>
        </form>
      </div></div>
    </div>
  `,
})
export class CustomerFormComponent {
  private readonly fb = inject(FormBuilder);
  private readonly service = inject(CustomerService);
  private readonly router = inject(Router);

  readonly saving = signal(false);
  readonly error = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    customerName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    email: ['', [Validators.required, Validators.email]],
    phone: ['', [Validators.required, Validators.pattern(/^\+?[0-9]{10,15}$/)]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    address: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(255)]],
    branch: ['Bengaluru', [Validators.required]],
    role: ['USER', [Validators.required]],
  });

  invalid(control: string): boolean {
    const c = this.form.get(control);
    return !!c && c.invalid && (c.dirty || c.touched);
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    this.error.set(null);
    const payload: AdminCustomerRequest = {
      ...this.form.getRawValue(),
      role: this.form.getRawValue().role as UserRole,
    };
    this.service.create(payload).subscribe({
      next: () => {
        this.saving.set(false);
        this.router.navigate(['/admin/customers']);
      },
      error: (err) => {
        this.saving.set(false);
        this.error.set(err?.error?.message ?? 'Could not create customer.');
      },
    });
  }
}
