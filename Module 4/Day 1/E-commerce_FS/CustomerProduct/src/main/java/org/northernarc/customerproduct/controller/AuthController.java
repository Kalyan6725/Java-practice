package org.northernarc.customerproduct.controller;

import jakarta.validation.Valid;
import org.northernarc.customerproduct.dto.*;
import org.northernarc.customerproduct.model.User;
import org.northernarc.customerproduct.repository.UserRepository;
import org.northernarc.customerproduct.service.CustomerServiceDao;
import org.northernarc.customerproduct.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.AccessDeniedException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    @Autowired
    CustomerServiceDao customerServiceDao;
    @Autowired
    UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody JwtRequestDTO request) {
        Authentication authentication= new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        authenticationManager.authenticate(authentication);

        // Get user from database to retrieve role
        User user = userRepository.findByUsernameIgnoreCase(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        JwtResponseDTO response = new JwtResponseDTO();
        // Generate token with username and role
        response.setToken(jwtUtil.generateToken(request.getUsername(), user.getRole().name()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        CustomerResponseDTO serviceResponse = customerServiceDao.registerCustomer(request);
        RegisterResponseDTO response = new RegisterResponseDTO(serviceResponse.getId(), serviceResponse.getName(), serviceResponse.getUsername(), serviceResponse.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<RegisterResponseDTO> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new AccessDeniedException("Invalid authentication context");
        }

        User user = userRepository.findByUsernameIgnoreCase(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("No logged-in user found"));

        RegisterResponseDTO response = new RegisterResponseDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getRole().name());
        return ResponseEntity.ok(response);
    }
}
