import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { JwtRequestDTO, JwtResponseDTO, RegisterRequestDTO, RegisterResponseDTO } from '../models/api.models';
import { TokenService } from './token.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = environment.apiUrl;
  private currentUserSubject = new BehaviorSubject<string | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private tokenService: TokenService,
    private router: Router
  ) {
    // Check if user is already logged in
    const token = this.tokenService.getToken();
    if (token) {
      const username = this.tokenService.getUsernameFromToken();
      this.currentUserSubject.next(username);
    }
  }

  /**
   * Register new user
   * Endpoint: POST /auth/register
   */
  register(registerData: RegisterRequestDTO): Observable<RegisterResponseDTO> {
    const url = `${this.API_URL}/auth/register`;
    return this.http.post<RegisterResponseDTO>(url, registerData);
  }

  /**
   * Login user with username and password
   * Endpoint: POST /auth/login
   */
  login(credentials: JwtRequestDTO): Observable<JwtResponseDTO> {
    const url = `${this.API_URL}${environment.endpoints.auth.login}`;
    
    return this.http.post<JwtResponseDTO>(url, credentials).pipe(
      tap(response => {
        console.log('🔑 [AuthService] Login response:', response);
        
        // Store token
        this.tokenService.saveToken(response.token);
        
        // Update current user
        const username = this.tokenService.getUsernameFromToken();
        console.log('🔑 [AuthService] Username from token:', username);
        
        this.currentUserSubject.next(username);
      })
    );
  }

  /**
   * Logout current user
   */
  logout(): void {
    this.tokenService.removeToken();
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  /**
   * Check if user is authenticated
   */
  isAuthenticated(): boolean {
    const token = this.tokenService.getToken();
    return token !== null && !this.tokenService.isTokenExpired();
  }

  /**
   * Get current username
   */
  getCurrentUsername(): string | null {
    return this.tokenService.getUsernameFromToken();
  }

  /**
   * Get current user roles
   */
  getCurrentUserRoles(): string[] {
    return this.tokenService.getRolesFromToken();
  }

  /**
   * Check if user has specific role
   */
  hasRole(role: string): boolean {
    const roles = this.getCurrentUserRoles();
    console.log(`🔐 [AuthService] Checking role '${role}'`);
    console.log('🔐 [AuthService] User roles:', roles);
    const hasRole = roles.includes(role);
    console.log(`🔐 [AuthService] Has role '${role}':`, hasRole);
    return hasRole;
  }

  /**
   * Check if user is admin
   */
  isAdmin(): boolean {
    console.log('👑 [AuthService] isAdmin() called');
    const result = this.hasRole('ROLE_ADMIN');
    console.log('👑 [AuthService] isAdmin() result:', result);
    return result;
  }

  /**
   * Check if user is regular user
   */
  isUser(): boolean {
    return this.hasRole('ROLE_USER');
  }
}
