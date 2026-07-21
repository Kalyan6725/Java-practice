import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { LoginService } from '../../services/login-service';
import { UserService } from '../../services/user-service';
import { RequestStatus, initialStatus, loadingStatus, successStatus, errorStatus } from '../../types/request-status.type';

@Component({
  selector: 'app-welcome-component',
  imports: [CommonModule, RouterModule],
  templateUrl: './welcome-component.html',
  styleUrl: './welcome-component.css',
})
export class WelcomeComponent implements OnInit {
  loginService: LoginService = inject(LoginService);
  userService: UserService = inject(UserService);
  router: Router = inject(Router);

  token: string | null = null;
  userStatus = signal<RequestStatus>(initialStatus);
  adminStatus = signal<RequestStatus>(initialStatus);
  userMessage = signal('');
  adminMessage = signal('');

  ngOnInit(): void {
    if (localStorage.getItem("token") == null) {
      this.router.navigate(["/login"]);
    } else {
      this.token = localStorage.getItem("token");
    }
  }

  testUserEndpoint(): void {
    this.userStatus.set(loadingStatus);
    console.log('Testing /user endpoint with token:', this.token);
    
    this.userService.getUserData().subscribe({
      next: (data) => {
        this.userMessage.set(data);
        this.userStatus.set(successStatus);
        console.log('User endpoint success:', data);
      },
      error: (err) => {
        console.error('User endpoint error:', err);
        let errorMessage = '';
        
        if (err.status === 403) {
          errorMessage = 'Access Forbidden: You do not have USER role';
        } else if (err.status === 401) {
          errorMessage = 'Unauthorized: Invalid or expired token';
        } else if (err.status === 0) {
          errorMessage = 'CORS or Network Error: Cannot reach backend';
        } else {
          errorMessage = err.error || `Error ${err.status}: ${err.statusText}`;
        }
        
        this.userStatus.set(errorStatus(errorMessage));
      }
    });
  }

  testAdminEndpoint(): void {
    this.adminStatus.set(loadingStatus);
    console.log('Testing /admin endpoint with token:', this.token);
    
    this.userService.getAdminData().subscribe({
      next: (data) => {
        this.adminMessage.set(data);
        this.adminStatus.set(successStatus);
        console.log('Admin endpoint success:', data);
      },
      error: (err) => {
        console.error('Admin endpoint error:', err);
        let errorMessage = '';
        
        if (err.status === 403) {
          errorMessage = 'Access Forbidden: You do not have ADMIN role';
        } else if (err.status === 401) {
          errorMessage = 'Unauthorized: Invalid or expired token';
        } else if (err.status === 0) {
          errorMessage = 'CORS or Network Error: Cannot reach backend';
        } else {
          errorMessage = err.error || `Error ${err.status}: ${err.statusText}`;
        }
        
        this.adminStatus.set(errorStatus(errorMessage));
      }
    });
  }
}
