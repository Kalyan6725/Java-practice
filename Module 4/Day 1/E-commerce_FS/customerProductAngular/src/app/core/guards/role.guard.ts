import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Role Guard Factory
 * Creates a guard that checks if user has required role
 */
export const roleGuard = (requiredRole: string): CanActivateFn => {
  return (route, state) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    if (!authService.isAuthenticated()) {
      router.navigate(['/login'], {
        queryParams: { returnUrl: state.url }
      });
      return false;
    }

    if (authService.hasRole(requiredRole)) {
      return true;
    }

    // User doesn't have required role
    router.navigate(['/']);
    return false;
  };
};

/**
 * Admin Guard
 * Ensures user has ADMIN role
 */
export const adminGuard: CanActivateFn = roleGuard('ROLE_ADMIN');

/**
 * User Guard
 * Ensures user has USER role (or any authenticated user)
 */
export const userGuard: CanActivateFn = roleGuard('ROLE_USER');
