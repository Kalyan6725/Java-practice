package org.northernarc.loanmanagementproject.controller;

import org.northernarc.loanmanagementproject.dto.LoginRequest;
import org.northernarc.loanmanagementproject.dto.LoginResponse;
import org.northernarc.loanmanagementproject.entity.Customer;
import org.northernarc.loanmanagementproject.repository.CustomerRepository;
import org.northernarc.loanmanagementproject.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

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

    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate using AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );
            
            // Generate JWT token
            String token = jwtUtil.generateToken(loginRequest.getEmail());
            
            return ResponseEntity.ok(new LoginResponse(
                token,
                "Login successful"
            ));
            
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponse(null, "Invalid email or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponse(null, "Authentication failed"));
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
    public ResponseEntity<?> register(@Valid @RequestBody Customer customer) {
        try {
            // Check if email already exists
            if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Email already registered");
            }
            
            // Encode password before saving
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            
            // Force role to USER (customer can't set their own role)
            customer.setRole("USER");
            
            Customer savedCustomer = customerRepository.save(customer);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedCustomer);
                
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Registration failed: " + e.getMessage());
        }
    }
    
    /**
     * Verify token endpoint - Check if JWT token is valid
     * 
     * @return status and message
     */
    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid authorization header");
            }
            
            String token = authHeader.substring(7);
            
            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                return ResponseEntity.ok("Token is valid for user: " + username);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token is invalid or expired");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token verification failed");
        }
    }
}
