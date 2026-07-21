// auth.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
 // 1. Retrieve the token from localStorage (using the same key as login component)
 const token = localStorage.getItem('token');
 
 // 2. If a token exists, clone the request and add the Authorization header
 if (token) {
   const clonedRequest = req.clone({
     setHeaders: {
       Authorization: `Bearer ${token}`
     }
   });
   console.log('Interceptor: Adding Authorization header to request:', req.url);
   // Pass the cloned request with the header to the next handler
   return next(clonedRequest);
 }
 
 // 3. If there is no token, let the original request pass through untouched
 console.log('Interceptor: No token found, request proceeding without auth:', req.url);
 return next(req);
};