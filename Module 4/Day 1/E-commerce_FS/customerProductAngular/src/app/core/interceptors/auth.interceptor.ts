import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenService } from '../services/token.service';

/**
 * JWT Auth Interceptor
 * Adds JWT token to all outgoing HTTP requests
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService = inject(TokenService);
  const token = tokenService.getToken();

  // Skip auth for public endpoints
  const isAuthEndpoint = req.url.includes('/auth/login') || req.url.includes('/auth/register');
  const isPublicProductEndpoint = req.url.includes('/api/products') && req.method === 'GET';
  
  if (isAuthEndpoint || isPublicProductEndpoint) {
    return next(req);
  }

  // Add token to request if available
  if (token) {
    const clonedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(clonedRequest);
  }

  return next(req);
};
