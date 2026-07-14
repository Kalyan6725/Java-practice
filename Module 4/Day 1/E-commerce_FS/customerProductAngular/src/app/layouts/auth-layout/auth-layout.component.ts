import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from '../../features/auth/pages/login/login.component';
import { RegisterComponent } from '../../features/auth/pages/register/register.component';

@Component({
  selector: 'app-auth-layout',
  standalone: true,
  imports: [CommonModule, LoginComponent, RegisterComponent],
  templateUrl: './auth-layout.component.html',
  styleUrl: './auth-layout.component.css'
})
export class AuthLayoutComponent {
  showLogin = true;

  toggleView(): void {
    this.showLogin = !this.showLogin;
  }
}
