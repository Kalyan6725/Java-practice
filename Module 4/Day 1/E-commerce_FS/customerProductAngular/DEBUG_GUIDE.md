# Debug Guide - Infinite Loading Issue

## What We Added

Comprehensive debugging checkpoints have been added throughout the application to identify where the infinite loading occurs.

## How to Debug

1. **Open Browser Console** (F12 or Right-click → Inspect → Console tab)

2. **Click on "Products" or "Shop Now"** button

3. **Watch the Console Logs** - You should see logs in this order:

### Expected Log Sequence:

```
================================================================================
🔵 [ProductListComponent] ngOnInit called - component initializing
🔵 [ProductListComponent] Backend URL: http://localhost:8080
================================================================================

🔵 [ProductListComponent] loadProducts() started
🔵 [ProductListComponent] Calling productService.getAllProducts()
🟢 [ProductService] getAllProducts() called
🟢 [ProductService] API URL: http://localhost:8080
🟢 [ProductService] Full endpoint: http://localhost:8080/api/products
🟢 [ProductService] Making HTTP GET request...
🟡 [AuthInterceptor] Intercepting request to: http://localhost:8080/api/products
🟡 [AuthInterceptor] Token present: true/false
🟡 [AuthInterceptor] Adding Bearer token to request (or No token available)
🟣 [LoadingInterceptor] Request started for: http://localhost:8080/api/products
🟣 [LoadingInterceptor] Loading indicator shown
⏳ [LoadingService] show() called - Request count: 1
⏳ [LoadingService] Loading state set to TRUE
🔴 [ErrorInterceptor] Monitoring request to: http://localhost:8080/api/products

... (Wait for response) ...

🟣 [LoadingInterceptor] Request completed for: http://localhost:8080/api/products
🟣 [LoadingInterceptor] Loading indicator hidden
⏳ [LoadingService] hide() called - Request count: 0
⏳ [LoadingService] Loading state set to FALSE
✅ [ProductListComponent] Received products: [...]
✅ [ProductListComponent] Number of products: X
✅ [ProductListComponent] Loading complete - products displayed
```

## Common Issues to Identify

### 1. **If logs stop at ProductService**
- Backend is not running
- Backend URL is incorrect
- Network connectivity issue
- **Solution**: Start your Spring Boot backend on port 8080

### 2. **If logs show timeout warning (after 5 seconds)**
```
⚠️ [ProductListComponent] Request taking longer than 5 seconds...
⚠️ [ProductListComponent] Check if backend is running on http://localhost:8080
```
- Backend is slow or not responding
- **Solution**: Check backend server status and logs

### 3. **If logs show error in ErrorInterceptor**
```
🔴 [ErrorInterceptor] Error caught for: http://localhost:8080/api/products
🔴 [ErrorInterceptor] HTTP Error Details: { status: 401/403/404/500, ... }
```
- **401**: Authentication required - Login first
- **403**: Forbidden - Check user permissions
- **404**: Endpoint not found - Check backend routes
- **500**: Server error - Check backend logs
- **0**: Network error - Backend not running

### 4. **If LoadingService request count never returns to 0**
- Check the LoadingService logs to see if hide() is being called
- Request might be hanging without completing or erroring

### 5. **If no logs appear at all**
- JavaScript error preventing component initialization
- Check browser console for red error messages

## Network Tab Verification

In addition to console logs, check the **Network tab**:

1. Open DevTools (F12) → **Network** tab
2. Click on "Products"
3. Look for the request to `http://localhost:8080/api/products`
4. Check the request status:
   - **Pending**: Request is hanging - backend not responding
   - **Failed**: Network error - backend not running
   - **200 OK**: Success - check why data isn't displaying
   - **401/403**: Authentication/authorization issue
   - **404**: Endpoint doesn't exist
   - **500**: Backend error

## Quick Fixes

### Backend Not Running
```bash
# Navigate to your Spring Boot project
cd path/to/backend
# Start the backend
mvn spring-boot:run
# OR
./mvnw spring-boot:run
```

### CORS Issues
If you see CORS errors in console, ensure your Spring Boot backend has:
```java
@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}
```

### Need to Login First
If you see 401 errors and need authentication:
1. Click "Login" in the navbar
2. Enter credentials
3. Try accessing products again

## Remove Debug Logs

Once you've identified the issue, you can remove all debug logs by searching for:
- `console.log` in the modified files
- Or keep them if you want ongoing debugging capability

## Files Modified with Debug Logs

1. [product-list.component.ts](src/app/features/products/pages/product-list/product-list.component.ts)
2. [product.service.ts](src/app/core/services/product.service.ts)
3. [auth.interceptor.ts](src/app/core/interceptors/auth.interceptor.ts)
4. [loading.interceptor.ts](src/app/core/interceptors/loading.interceptor.ts)
5. [error.interceptor.ts](src/app/core/interceptors/error.interceptor.ts)
6. [loading.service.ts](src/app/core/services/loading.service.ts)
