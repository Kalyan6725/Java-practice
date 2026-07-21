import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  template: `
    <div class="auth-wrap">
      <div class="auth-card card">
        <div class="card-body">
          <div class="auth-head">
            <div class="brand" style="justify-content:center">
              <span class="mark"><span class="tri"></span><span class="star">✦</span></span>
              <span class="name"><b>N<span class="dot">O</span>RTHERN</b><span>ARC</span></span>
            </div>
            <h2 class="mt-2 mb-0">Welcome back</h2>
            <p class="muted">Sign in to your account</p>
          </div>

          @if (error()) {
            <div class="alert alert-warning">{{ error() }}</div>
          }

          <form [formGroup]="form" (ngSubmit)="submit()">
            <div class="form-group">
              <label>Email <span class="req">*</span></label>
              <input class="input" type="email" formControlName="email" />
              @if (invalid('email')) {
                <div class="hint" style="color:var(--danger)">Enter a valid email.</div>
              }
            </div>
            <div class="form-group">
              <label>Password <span class="req">*</span></label>
              <input class="input" type="password" formControlName="password" />
              @if (invalid('password')) {
                <div class="hint" style="color:var(--danger)">Password is required.</div>
              }
            </div>
            <button type="submit" class="btn btn-primary btn-block btn-lg" [disabled]="loading()">
              {{ loading() ? 'Signing in…' : 'Sign in' }}
            </button>
          </form>

          <p class="text-center mt-2 mb-0" style="font-size:.9rem">
            New here? <a routerLink="/register">Create an account</a>
          </p>
        </div>
      </div>
    </div>
  `,
})
export class LoginComponent {
  private readonly fb = inject(FormBuilder);
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);

  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]],
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
    this.auth.login(this.form.getRawValue()).subscribe({
      next: (user) => {
        this.loading.set(false);
        const staff = ['UNDERWRITER', 'MANAGER', 'ADMIN'].includes(user.role);
        this.router.navigate([staff ? '/admin/dashboard' : '/customer/dashboard']);
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(err?.error?.message ?? 'Invalid email or password.');
      },
    });
  }
}
