package org.northernarc.loanmanagementproject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

/**
 * SecurityConfig - Spring Security configuration for JWT-based authentication with RBAC
 * 
 * Configures:
 * - JWT-based stateless authentication
 * - Role-Based Access Control (RBAC) with @PreAuthorize
 * - PasswordEncoder (BCrypt)
 * - AuthenticationManager
 * - SecurityFilterChain with JWT filter
 * - CORS configuration
 * - Security rules (permit /auth/login, authenticate everything else)
 * 
 * Roles:
 * - ADMIN: Full access to admin operations
 * - MANAGER: Loan management and approval
 * - USER: Basic user operations
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
public class SecurityConfig {
    
    @Autowired
    private JwtFilter jwtFilter;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    /**
     * PasswordEncoder bean - BCrypt password encoding
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * AuthenticationManager bean - Main authentication manager for the application
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    /**
     * SecurityFilterChain bean - Configures security rules and filters
     * 
     * Rules:
     * - POST /api/auth/login: Permit all (login endpoint)
     * - POST /api/auth/register: Permit all (registration endpoint)
     * - All /api/loan/** endpoints: Require authentication (role-based via @PreAuthorize)
     * - All other endpoints: Permit all (can be restricted later)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().write(errorJson("Authentication required", "UNAUTHORIZED", request));
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().write(errorJson("Access denied", "FORBIDDEN", request));
                })
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/register").permitAll()
                .requestMatchers("/api/auth/verify").permitAll()
                .requestMatchers("/api/loan/**").authenticated()
                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    /**
     * CORS Configuration - Allows cross-origin requests from all origins
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "X-Request-Id"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private String errorJson(String message, String code, jakarta.servlet.http.HttpServletRequest request) {
        Object requestIdObj = request.getAttribute("requestId");
        String requestId = requestIdObj != null ? requestIdObj.toString() : "";
        String timestamp = java.time.LocalDateTime.now().toString();
        return "{\"success\":false,\"message\":\"" + escapeJson(message) + "\",\"data\":null," +
                "\"error\":{\"code\":\"" + escapeJson(code) + "\",\"details\":null}," +
                "\"meta\":{\"timestamp\":\"" + escapeJson(timestamp) + "\",\"requestId\":\"" + escapeJson(requestId) + "\"}}";
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
