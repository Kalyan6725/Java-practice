import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { UserRole } from '../models/auth.model';

/** Blocks a route unless the user is signed in. */
export const authGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);
  if (auth.isLoggedIn()) return true;
  return router.createUrlTree(['/login']);
};

/**
 * Factory guard restricting a route to the given roles.
 * Usage: canActivate: [authGuard, roleGuard(['ADMIN', 'MANAGER'])]
 */
export function roleGuard(roles: UserRole[]): CanActivateFn {
  return () => {
    const auth = inject(AuthService);
    const router = inject(Router);
    if (auth.hasAnyRole(roles)) return true;
    // Signed in but wrong role -> send to their own home.
    return router.createUrlTree([auth.isStaff() ? '/admin/dashboard' : '/customer/dashboard']);
  };
}

/** Convenience guard for the whole staff area. */
export const staffGuard = roleGuard(['UNDERWRITER', 'MANAGER', 'ADMIN']);
