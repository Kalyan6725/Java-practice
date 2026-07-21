import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { finalize } from 'rxjs';
import { LoadingService } from '../services/loading.service';

/**
 * Loading Interceptor
 * Shows/hides loading indicator for HTTP requests
 */
export const loadingInterceptor: HttpInterceptorFn = (req, next) => {
  const loadingService = inject(LoadingService);
  console.log(' [LoadingInterceptor] Request started for:', req.url);

  // Start loading
  loadingService.show();
  console.log(' [LoadingInterceptor] Loading indicator shown');

  return next(req).pipe(
    finalize(() => {
      // Stop loading when request completes (success or error)
      console.log(' [LoadingInterceptor] Request completed for:', req.url);
      loadingService.hide();
      console.log(' [LoadingInterceptor] Loading indicator hidden');
    })
  );
};
