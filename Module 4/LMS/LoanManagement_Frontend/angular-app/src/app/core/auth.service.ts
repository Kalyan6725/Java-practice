import { Injectable, computed, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, tap } from 'rxjs';
import { switchMap, map, catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { ApiResponse } from '../models/api-response.model';
import {
  AuthUser,
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  UserRole,
} from '../models/auth.model';
import { Customer } from '../models/customer.model';

const TOKEN_KEY = 'lms_token';
const USER_KEY = 'lms_user';
const STAFF_ROLES: UserRole[] = ['UNDERWRITER', 'MANAGER', 'ADMIN'];

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly baseUrl = `${environment.apiUrl}/auth`;

  /** Reactive current-user state. */
  private readonly _user = signal<AuthUser | null>(this.readStoredUser());
  readonly user = this._user.asReadonly();
  readonly isLoggedIn = computed(() => this._user() !== null);
  readonly isStaff = computed(() => {
    const u = this._user();
    return !!u && STAFF_ROLES.includes(u.role);
  });

  constructor(private http: HttpClient) {}

  /** Authenticate, store the JWT, then load the profile to resolve role + id. */
  login(credentials: LoginRequest): Observable<AuthUser> {
    return this.http
      .post<ApiResponse<LoginResponse>>(`${this.baseUrl}/login`, credentials)
      .pipe(
        tap((res) => this.storeToken(res.data.token)),
        switchMap(() => this.loadProfile()),
      );
  }

  register(payload: RegisterRequest): Observable<Customer> {
    return this.http
      .post<ApiResponse<Customer>>(`${this.baseUrl}/register`, payload)
      .pipe(map((res) => res.data));
  }

  /** GET /api/auth/me -> resolves the signed-in identity. */
  loadProfile(): Observable<AuthUser> {
    return this.http.get<ApiResponse<Customer>>(`${this.baseUrl}/me`).pipe(
      map((res) => {
        const c = res.data;
        const user: AuthUser = {
          email: c.email,
          role: c.role,
          customerId: c.customerId,
          customerName: c.customerName,
        };
        this.storeUser(user);
        return user;
      }),
    );
  }

  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    this._user.set(null);
  }

  get token(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  hasAnyRole(roles: UserRole[]): boolean {
    const u = this._user();
    return !!u && roles.includes(u.role);
  }

  private storeToken(token: string): void {
    localStorage.setItem(TOKEN_KEY, token);
  }

  private storeUser(user: AuthUser): void {
    localStorage.setItem(USER_KEY, JSON.stringify(user));
    this._user.set(user);
  }

  private readStoredUser(): AuthUser | null {
    try {
      const raw = localStorage.getItem(USER_KEY);
      return raw ? (JSON.parse(raw) as AuthUser) : null;
    } catch {
      return null;
    }
  }
}
