# Backend Configuration Guide

## CORS Configuration Required

Your Spring Boot backend needs to allow CORS requests from your Angular frontend. Add this configuration to your backend:

### Option 1: WebMvcConfigurer (Recommended)

Create a new configuration class in your backend:

```java
package org.northernarc.jwt_demo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200", "http://localhost:*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

### Option 2: Add @CrossOrigin to Controller

Add this annotation to your AuthController:

```java
@RestController
@RequestMapping
@EnableMethodSecurity
@CrossOrigin(origins = "http://localhost:*", allowCredentials = "true")
public class AuthController {
    // ... your existing code
}
```

### Option 3: Security Configuration

If you're using Spring Security (which you are for JWT), add CORS to your SecurityFilterChain:

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())
        // ... rest of your security config
    return http.build();
}

@Bean
CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:64329"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
```

## Test User Account

Make sure you have a test user in your database with the appropriate roles:

- Username: `user` or `admin` (depending on what you set up)
- Password: Your configured password
- Roles: `ROLE_USER` or `ROLE_ADMIN`

The backend JWT token should contain the user's roles, and Spring Security will automatically check them when accessing protected endpoints.

## Verify Backend is Running

1. Make sure your backend is running on `http://localhost:8080`
2. Test the login endpoint directly:

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"youruser","password":"yourpass"}'
```

3. You should get a response like:
```json
{"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."}
```

4. Test the protected endpoint with the token:
```bash
curl http://localhost:8080/user \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## Common Issues

1. **CORS Error (Status 0)**: Backend not allowing cross-origin requests
   - Solution: Add CORS configuration as shown above

2. **401 Unauthorized**: Token is invalid or expired
   - Solution: Check JWT token generation and validation in backend

3. **403 Forbidden**: User doesn't have required role
   - Solution: Verify user has ROLE_USER or ROLE_ADMIN in database

4. **404 Not Found**: Endpoint path mismatch
   - Solution: Verify backend endpoints match frontend service URLs
