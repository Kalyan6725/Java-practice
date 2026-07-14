package org.northernarc.loanmanagementproject.controller;

import jakarta.validation.Valid;
import org.northernarc.loanmanagementproject.dto.request.LoginRequest;
import org.northernarc.loanmanagementproject.dto.response.ApiResponse;
import org.northernarc.loanmanagementproject.dto.response.LoginResponse;
import org.northernarc.loanmanagementproject.entity.Customer;
import org.northernarc.loanmanagementproject.repository.CustomerRepository;
import org.northernarc.loanmanagementproject.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * AuthController - Handles authentication endpoints
 * 
 * Endpoints:
 * - POST /api/auth/login - Authenticate and get JWT token
 * - POST /api/auth/register - Register new customer
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Login endpoint - Authenticate user and return JWT token
     * 
     * @param loginRequest containing email and password
     * @return JWT token in response
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );
            
            // Generate JWT token
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
    
    /**
     * Register endpoint - Customer self-registration
     * Password is encoded here before saving
     * Role always defaults to "USER" (can't be overridden by customer)
     * 
     * @param customer with email and password
     * @return newly created customer
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Customer>> register(@Valid @RequestBody Customer customer) {
        try {
            // Check if email already exists
            if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Email already registered", "BAD_REQUEST", null));
            }
            
            // Encode password before saving
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            
            // Force role to USER (customer can't set their own role)
            customer.setRole("USER");
            
            Customer savedCustomer = customerRepository.save(customer);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer registered successfully", savedCustomer));
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Registration failed", "BAD_REQUEST", e.getMessage()));
        }
    }
    
    /**
     * Verify token endpoint - Check if JWT token is valid
     * 
     * @return status and message
     */
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Map<String, String>>> verifyToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Invalid authorization header", "BAD_REQUEST", null));
            }
            
            String token = authHeader.substring(7);
            
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                return ResponseEntity.ok(ApiResponse.success(
                        "Token is valid",
                        Map.of("username", username, "status", "valid")
                ));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Token is invalid or expired", "UNAUTHORIZED", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Token verification failed", "UNAUTHORIZED", null));
        }
    }
}
