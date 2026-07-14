import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenService } from '../services/token.service';

/**
 * JWT Auth Interceptor
 * Adds JWT token to all outgoing HTTP requests
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  console.log('🟡 [AuthInterceptor] Intercepting request to:', req.url);
  const tokenService = inject(TokenService);
  const token = tokenService.getToken();
  console.log('🟡 [AuthInterceptor] Token present:', !!token);

  // Skip auth for public endpoints
  const publicEndpoints = ['/auth/login', '/auth/register', '/api/products'];
  const isPublicEndpoint = publicEndpoints.some(endpoint => req.url.includes(endpoint));
  
  if (isPublicEndpoint) {
    console.log('🟡 [AuthInterceptor] Skipping auth for public endpoint:', req.url);
    return next(req);
  }

  // Add token to request if available
  if (token) {
    console.log('🟡 [AuthInterceptor] Adding Bearer token to request');
    const clonedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(clonedRequest);
  }

  console.log('🟡 [AuthInterceptor] No token available - proceeding without auth');
  return next(req);
};
