import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <div class="auth-wrap">
      <div class="auth-card card" style="max-width:520px">
        <div class="card-body">
          <div class="auth-head">
            <div class="brand" style="justify-content:center">
              <span class="mark"><span class="tri"></span><span class="star">✦</span></span>
              <span class="name"><b>N<span class="dot">O</span>RTHERN</b><span>ARC</span></span>
            </div>
            <h2 class="mt-2 mb-0">Create your account</h2>
            <p class="muted">It only takes a minute</p>
          </div>

          @if (error()) {
            <div class="alert alert-warning">{{ error() }}</div>
          }

          <form [formGroup]="form" (ngSubmit)="submit()">
            <div class="form-group">
              <label>Full name <span class="req">*</span></label>
              <input class="input" formControlName="customerName" />
              @if (invalid('customerName')) {
                <div class="hint" style="color:var(--danger)">2–100 characters required.</div>
              }
            </div>
            <div class="form-row">
              <div class="form-group">
                <label>Email <span class="req">*</span></label>
                <input class="input" type="email" formControlName="email" />
                @if (invalid('email')) {
                  <div class="hint" style="color:var(--danger)">Enter a valid email.</div>
                }
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
              <label>Password <span class="req">*</span></label>
              <input class="input" type="password" formControlName="password" />
              @if (invalid('password')) {
                <div class="hint" style="color:var(--danger)">Minimum 6 characters.</div>
              }
            </div>
            <div class="form-group">
              <label>Address <span class="req">*</span></label>
              <input class="input" formControlName="address" />
              @if (invalid('address')) {
                <div class="hint" style="color:var(--danger)">5–255 characters required.</div>
              }
            </div>
            <div class="form-group">
              <label>Branch <span class="req">*</span></label>
              <select class="select" formControlName="branch">
                <option>Bengaluru</option><option>Mumbai</option><option>Delhi</option>
                <option>Chennai</option><option>Hyderabad</option>
              </select>
            </div>
            <button type="submit" class="btn btn-primary btn-block btn-lg" [disabled]="loading()">
              {{ loading() ? 'Creating…' : 'Create account' }}
            </button>
          </form>

          <p class="text-center mt-2 mb-0" style="font-size:.9rem">
            Already have an account? <a routerLink="/login">Sign in</a>
          </p>
        </div>
      </div>
    </div>
  `,
})
export class RegisterComponent {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    customerName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    phone: ['', [Validators.required, Validators.pattern(/^\+?[0-9]{10,15}$/)]],
    address: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(255)]],
    branch: ['Bengaluru', [Validators.required]],
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
    this.loading.set(true);
    this.error.set(null);
    this.auth.register(this.form.getRawValue()).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(err?.error?.message ?? 'Registration failed. Please try again.');
      },
    });
  }
}
