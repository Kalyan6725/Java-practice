import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  template: `
    <div class="login-container">
      <div class="login-card">
        <h2>Login</h2>
        <form>
          <div class="form-group">
            <label>Email</label>
            <input type="email" class="form-control" placeholder="Enter email">
          </div>
          <div class="form-group">
            <label>Password</label>
            <input type="password" class="form-control" placeholder="Enter password">
          </div>
          <button type="submit" class="btn btn-primary w-100">Login</button>
        </form>
        <div class="mt-3 text-center">
          <p>Don't have an account? <a routerLink="/register">Register</a></p>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .login-container {
      min-height: calc(100vh - 120px);
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 2rem;
    }
    .login-card {
      max-width: 400px;
      width: 100%;
      padding: 2rem;
      background: white;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.1);
    }
  `]
})
export class LoginComponent {
}
