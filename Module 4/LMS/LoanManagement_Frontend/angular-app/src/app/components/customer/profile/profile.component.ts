import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CustomerService } from '../../../services/customer.service';
import { AuthService } from '../../../core/auth.service';
import { Customer } from '../../../models/customer.model';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    <div class="page container">
      <div class="page-head"><div><h1>My profile</h1><p class="sub">Manage your personal details.</p></div></div>

      @if (saved()) { <div class="alert alert-success">Profile updated successfully.</div> }
      @if (error()) { <div class="alert alert-warning">{{ error() }}</div> }

      @if (loading()) {
        <p class="muted">Loading profile…</p>
      } @else if (customer(); as c) {
        <div class="row">
          <div class="col" style="max-width:320px">
            <div class="card"><div class="card-body text-center">
              <div style="width:80px;height:80px;border-radius:50%;background:var(--space-blue-soft);color:var(--space-blue-dark);
                          display:inline-flex;align-items:center;justify-content:center;font-size:1.8rem;font-weight:800">{{ initials() }}</div>
              <h3 class="mt-2 mb-0">{{ c.customerName }}</h3>
              <p class="muted">Customer • ID #{{ c.customerId }}</p>
              <span class="badge badge-info">{{ c.role }}</span>
              <hr class="divider" />
              <dl class="dl" style="grid-template-columns:90px 1fr;text-align:left">
                <dt>Branch</dt><dd>{{ c.branch }}</dd>
                <dt>Status</dt><dd>{{ c.status }}</dd>
              </dl>
            </div></div>
          </div>

          <div class="col">
            <div class="card"><div class="card-body">
              <h3>Edit details</h3>
              <form [formGroup]="form" (ngSubmit)="submit()">
                <div class="form-group">
                  <label>Full name <span class="req">*</span></label>
                  <input class="input" formControlName="customerName" />
                </div>
                <div class="form-row">
                  <div class="form-group">
                    <label>Email</label>
                    <input class="input" type="email" [value]="c.email" disabled />
                    <div class="hint">Email cannot be changed.</div>
                  </div>
                  <div class="form-group">
                    <label>Phone <span class="req">*</span></label>
                    <input class="input" formControlName="phone" />
                    @if (invalid('phone')) {
                      <div class="hint" style="color:var(--danger)">10–15 digits, may start with +.</div>
                    }
                  </div>
                </div>
                <div class="form-group">
                  <label>Address <span class="req">*</span></label>
                  <input class="input" formControlName="address" />
                </div>
                <div class="form-group">
                  <label>Branch <span class="req">*</span></label>
                  <select class="select" formControlName="branch">
                    <option>Bengaluru</option><option>Mumbai</option><option>Delhi</option>
                    <option>Chennai</option><option>Hyderabad</option>
                  </select>
                </div>
                <button type="submit" class="btn btn-primary" [disabled]="saving()">
                  {{ saving() ? 'Saving…' : 'Save changes' }}
                </button>
              </form>
            </div></div>
          </div>
        </div>
      }
    </div>
  `,
})
export class ProfileComponent {
  private readonly fb = inject(FormBuilder);
  private readonly service = inject(CustomerService);
  private readonly auth = inject(AuthService);

  readonly customer = signal<Customer | null>(null);
  readonly loading = signal(true);
  readonly saving = signal(false);
  readonly saved = signal(false);
  readonly error = signal<string | null>(null);

  readonly initials = computed(() => {
    const name = this.customer()?.customerName ?? '';
    return name.split(' ').map((n) => n[0]).join('').slice(0, 2).toUpperCase();
  });

  readonly form = this.fb.nonNullable.group({
    customerName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    phone: ['', [Validators.required, Validators.pattern(/^\+?[0-9]{10,15}$/)]],
    address: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(255)]],
    branch: ['', [Validators.required]],
  });

  invalid(control: string): boolean {
    const c = this.form.get(control);
    return !!c && c.invalid && (c.dirty || c.touched);
  }

  constructor() {
    this.service.getMe().subscribe({
      next: (c) => {
        this.customer.set(c);
        this.form.patchValue({
          customerName: c.customerName,
          phone: c.phone,
          address: c.address,
          branch: c.branch,
        });
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Unable to load your profile.');
        this.loading.set(false);
      },
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    this.saved.set(false);
    this.error.set(null);
    this.service.updateMe(this.form.getRawValue()).subscribe({
      next: (c) => {
        this.customer.set(c);
        this.saving.set(false);
        this.saved.set(true);
        this.auth.loadProfile().subscribe();
      },
      error: (err) => {
        this.saving.set(false);
        this.error.set(err?.error?.message ?? 'Could not update your profile.');
      },
    });
  }
}
