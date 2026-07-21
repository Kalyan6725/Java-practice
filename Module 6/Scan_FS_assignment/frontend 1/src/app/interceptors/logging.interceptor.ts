import { HttpInterceptorFn } from '@angular/common/http';
import { tap } from 'rxjs/operators';

export const loggingInterceptor: HttpInterceptorFn = (req, next) => {
  const startTime = Date.now();

  console.log(`[HTTP Request] ${req.method} ${req.url}`, {
    body: req.body,
    params: req.params.toString(),
    headers: req.headers.keys()
  });

  return next(req).pipe(
    tap({
      next: (event) => {
        if (event.type !== 0) {
          const elapsed = Date.now() - startTime;
          console.log(`[HTTP Response] ${req.method} ${req.url} - ${elapsed}ms`, event);
        }
      },
      error: (error) => {
        const elapsed = Date.now() - startTime;
        console.error(`[HTTP Error] ${req.method} ${req.url} - ${elapsed}ms`, {
          status: error.status,
          statusText: error.statusText,
          message: error.message,
          error: error.error
        });
      }
    })
  );
};
