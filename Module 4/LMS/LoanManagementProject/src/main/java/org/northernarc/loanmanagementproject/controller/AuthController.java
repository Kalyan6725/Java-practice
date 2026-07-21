package org.northernarc.loanmanagementproject.controller;

import jakarta.validation.Valid;
import org.northernarc.loanmanagementproject.dto.DTOMapper;
import org.northernarc.loanmanagementproject.dto.request.LoginRequest;
import org.northernarc.loanmanagementproject.dto.request.RegisterRequest;
import org.northernarc.loanmanagementproject.dto.response.ApiResponse;
import org.northernarc.loanmanagementproject.dto.response.CustomerResponse;
import org.northernarc.loanmanagementproject.dto.response.LoginResponse;
import org.northernarc.loanmanagementproject.entity.Customer;
import org.northernarc.loanmanagementproject.service.CustomerService;
import org.northernarc.loanmanagementproject.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * AuthController - Authentication and self-service registration.
 *
 * Endpoints:
 * - POST /api/auth/register - Customer self-registration (role forced to USER)
 * - POST /api/auth/login    - Authenticate and receive a JWT
 * - GET  /api/auth/verify   - Validate a JWT
 * - GET  /api/auth/me       - Current authenticated profile
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomerService customerService;
    private final JwtUtil jwtUtil;
    private final DTOMapper dtoMapper;

    public AuthController(AuthenticationManager authenticationManager,
                         CustomerService customerService,
                         JwtUtil jwtUtil,
                         DTOMapper dtoMapper) {
        this.authenticationManager = authenticationManager;
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<CustomerResponse>> register(@Valid @RequestBody RegisterRequest request) {
        Customer saved = customerService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer registered successfully",
                        dtoMapper.toCustomerResponse(saved)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            String token = jwtUtil.generateToken(loginRequest.getEmail());
            LoginResponse payload = new LoginResponse(token, "Login successful");
            return ResponseEntity.ok(ApiResponse.success("Login successful", payload));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid email or password", "UNAUTHORIZED", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Authentication failed", "UNAUTHORIZED", null));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Map<String, String>>> verifyToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Invalid authorization header", "BAD_REQUEST", null));
        }
        String token = authHeader.substring(7);
        if (jwtUtil.validateToken(token)) {
            String username = jwtUtil.extractUsername(token);
            return ResponseEntity.ok(ApiResponse.success(
                    "Token is valid",
                    Map.of("username", username, "status", "valid")));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Token is invalid or expired", "UNAUTHORIZED", null));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CustomerResponse>> me() {
        org.springframework.security.core.Authentication auth =
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Not authenticated", "UNAUTHORIZED", null));
        }
        Customer customer = customerService.getByEmail(auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Profile fetched successfully",
                dtoMapper.toCustomerResponse(customer)));
    }
}
