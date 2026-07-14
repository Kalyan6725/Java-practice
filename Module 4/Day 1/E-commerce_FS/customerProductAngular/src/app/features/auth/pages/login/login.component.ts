import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  @Output() switchToRegister = new EventEmitter<void>();
  
  loginForm!: FormGroup;
  loading = false;
  errorMessage = '';
  returnUrl = '/';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // Initialize form
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });

    // Get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';

    // Redirect if already logged in
    if (this.authService.isAuthenticated()) {
      if (this.authService.isAdmin()) {
        this.router.navigate(['/admin/dashboard']);
      } else {
        this.router.navigate([this.returnUrl]);
      }
    }
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.markFormGroupTouched(this.loginForm);
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    const credentials = this.loginForm.value;

    this.authService.login(credentials).subscribe({
      next: (response) => {
        console.log('✅ Login successful:', response);
        
        // Debug: Check role detection
        const roles = this.authService.getCurrentUserRoles();
        const isAdmin = this.authService.isAdmin();
        console.log('🔍 User roles:', roles);
        console.log('🔍 Is admin?', isAdmin);
        
        // Redirect based on user role
        if (isAdmin) {
          // Admin user → redirect to admin dashboard
          console.log('🎯 Redirecting to: /admin/dashboard');
          this.router.navigate(['/admin/dashboard']);
        } else {
          // Regular user → redirect to returnUrl or home
          console.log('🎯 Redirecting to:', this.returnUrl);
          this.router.navigate([this.returnUrl]);
        }
      },
      error: (error) => {
        console.error('Login failed:', error);
        this.errorMessage = error.message || 'Login failed. Please check your credentials.';
        this.loading = false;
      },
      complete: () => {
        this.loading = false;
      }
    });
  }

  onSwitchToRegister(): void {
    this.switchToRegister.emit();
  }

  // Helper method to mark all fields as touched
  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
    });
  }

  // Getter methods for form validation
  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password');
  }
}
