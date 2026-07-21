import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

function extractMessage(error: unknown): string | null {
  if (!error) return null;
  if (typeof error === 'string') return error;
  if (typeof error === 'object' && 'message' in error && typeof (error as Record<string, unknown>)['message'] === 'string') {
    return (error as Record<string, string>)['message'];
  }
  return null;
}

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let userMessage = 'An unexpected error occurred. Please try again.';

      if (error.status === 0) {
        userMessage = 'Unable to connect to the server. Please check if the backend is running.';
      } else if (error.status === 400) {
        userMessage = extractMessage(error.error) || 'Invalid request. Please check your input.';
      } else if (error.status === 404) {
        userMessage = extractMessage(error.error) || 'The requested resource was not found.';
      } else if (error.status === 409) {
        userMessage = extractMessage(error.error) || 'A conflict occurred with the current state.';
      } else if (error.status >= 500) {
        userMessage = 'A server error occurred. Please try again later.';
      }

      const enhancedError = new HttpErrorResponse({
        error: { message: userMessage, originalError: error.error },
        headers: error.headers,
        status: error.status,
        statusText: error.statusText,
        url: error.url || undefined
      });

      return throwError(() => enhancedError);
    })
  );
};
