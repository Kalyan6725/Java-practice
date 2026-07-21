import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private readonly TOKEN_KEY = 'auth_token';

  constructor() { }

  /**
   * Save JWT token to localStorage
   */
  saveToken(token: string): void {
    console.log(' [TokenService] Saving token:', token.substring(0, 50) + '...');
    localStorage.setItem(this.TOKEN_KEY, token);
    
    // Immediately decode and log for debugging
    const decoded = this.decodeToken(token);
    console.log(' [TokenService] Decoded token payload:', decoded);
  }

  /**
   * Get JWT token from localStorage
   */
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  /**
   * Remove JWT token from localStorage
   */
  removeToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  /**
   * Decode JWT token payload
   */
  private decodeToken(token: string): any {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      return JSON.parse(jsonPayload);
    } catch (error) {
      console.error('Error decoding token:', error);
      return null;
    }
  }

  /**
   * Get username from JWT token
   */
  getUsernameFromToken(): string | null {
    const token = this.getToken();
    if (!token) return null;

    const decoded = this.decodeToken(token);
    return decoded?.sub || decoded?.username || null;
  }

  /**
   * Get roles from JWT token
   */
  getRolesFromToken(): string[] {
    const token = this.getToken();
    if (!token) {
      console.log(' [TokenService] getRolesFromToken: No token found');
      return [];
    }

    const decoded = this.decodeToken(token);
    console.log(' [TokenService] Decoded token for roles:', decoded);
    
    const roles = decoded?.roles || decoded?.authorities || [];
    console.log(' [TokenService] Extracted roles:', roles);
    console.log(' [TokenService] decoded.roles:', decoded?.roles);
    console.log(' [TokenService] decoded.authorities:', decoded?.authorities);
    
    return roles;
  }

  /**
   * Check if token is expired
   */
  isTokenExpired(): boolean {
    const token = this.getToken();
    if (!token) return true;

    const decoded = this.decodeToken(token);
    if (!decoded || !decoded.exp) return true;

    const expirationDate = new Date(decoded.exp * 1000);
    return expirationDate < new Date();
  }

  /**
   * Get token expiration date
   */
  getTokenExpirationDate(): Date | null {
    const token = this.getToken();
    if (!token) return null;

    const decoded = this.decodeToken(token);
    if (!decoded || !decoded.exp) return null;

    return new Date(decoded.exp * 1000);
  }
}
