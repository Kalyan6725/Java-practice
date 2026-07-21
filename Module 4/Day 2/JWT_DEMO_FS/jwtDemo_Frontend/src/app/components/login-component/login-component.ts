import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms'; // Required for [(ngModel)] usage
import JwtRequestDTO from '../../dto/JwtRequestDTO';
import { LoginService } from '../../services/login-service';
import { RequestStatus, initialStatus, loadingStatus, successStatus, errorStatus } from '../../types/request-status.type';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './login-component.html',
  styles: [`
    :host {
      display: block;
      width: 100%;
    }
  `]
})
export class LoginComponent implements OnInit{
  loginService:LoginService = inject(LoginService);
  
  // Simple data object for two-way binding mapping to backend DTO structure
  credentials:JwtRequestDTO = {
    username: '',
    password: ''
  };

  formSubmitted = false;
  hidePassword = true;
  loginStatus = signal<RequestStatus>(initialStatus);

  constructor(private router: Router) {}
  ngOnInit(): void {
   
  if(localStorage.getItem("token")!=null)
    this.router.navigate(["/welcome"]);
}
  

  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }

  onSubmit(isValid: boolean | null): void {
    this.formSubmitted = true;
    this.loginStatus.set(initialStatus);

    // Check HTML5 structural constraint validation before proceeding
    if (!isValid) {
      return;
    }

    this.loginStatus.set(loadingStatus);
    
    // Direct payload access via model bindings
    console.log('Dispatched Action: POST /auth/login', this.credentials);
    this.loginService.login(this.credentials).subscribe({
      next:(data)=>{
        console.log('Login successful, received token:', data);
        localStorage.setItem("token", data.token);
        console.log('Token stored in localStorage with key: "token"');
        this.loginStatus.set(successStatus);
        this.router.navigate(['/welcome']);
      },
      error:(err)=>{
        console.error('Login error:', err);
        let errorMessage = '';
        
        if (err.status === 401) {
          errorMessage = 'Invalid username or password';
        } else if (err.status === 0) {
          errorMessage = 'Cannot connect to server. Make sure backend is running on http://localhost:8080';
        } else {
          errorMessage = err.error?.message || `Error ${err.status}: ${err.statusText}`;
        }
        
        this.loginStatus.set(errorStatus(errorMessage));
      }
    });
  }
}