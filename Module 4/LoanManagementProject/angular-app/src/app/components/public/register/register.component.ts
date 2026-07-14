import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  template: `
    <div class="register-container">
      <div class="register-card">
        <h2>Register</h2>
        <form>
          <div class="form-group">
            <label>Full Name</label>
            <input type="text" class="form-control" placeholder="Enter full name">
          </div>
          <div class="form-group">
            <label>Email</label>
            <input type="email" class="form-control" placeholder="Enter email">
          </div>
          <div class="form-group">
            <label>Password</label>
            <input type="password" class="form-control" placeholder="Enter password">
          </div>
          <div class="form-group">
            <label>Confirm Password</label>
            <input type="password" class="form-control" placeholder="Confirm password">
          </div>
          <button type="submit" class="btn btn-primary w-100">Register</button>
        </form>
        <div class="mt-3 text-center">
          <p>Already have an account? <a routerLink="/login">Login</a></p>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .register-container {
      min-height: calc(100vh - 120px);
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 2rem;
    }
    .register-card {
      max-width: 400px;
      width: 100%;
      padding: 2rem;
      background: white;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.1);
    }
  `]
})
export class RegisterComponent {
}
