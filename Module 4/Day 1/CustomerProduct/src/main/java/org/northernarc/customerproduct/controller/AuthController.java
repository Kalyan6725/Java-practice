package org.northernarc.customerproduct.controller;

import jakarta.validation.Valid;
import org.northernarc.customerproduct.dto.JwtRequestDTO;
import org.northernarc.customerproduct.dto.JwtResponseDTO;
import org.northernarc.customerproduct.utility.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody JwtRequestDTO request) {
        Authentication authentication= new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        authenticationManager.authenticate(authentication);

        JwtResponseDTO response = new JwtResponseDTO();
        response.setToken(jwtUtil.generateToken(request.getUsername()));
        return ResponseEntity.ok(response);
    }
}
