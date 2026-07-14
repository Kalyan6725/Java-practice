package org.northernarc.jwt_demo.controller;

import org.northernarc.jwt_demo.dto.JwtRequestDTO;
import org.northernarc.jwt_demo.dto.JwtResponseDTO;
import org.northernarc.jwt_demo.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@EnableMethodSecurity
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @PostMapping("/auth/login")
    public JwtResponseDTO login(@RequestBody JwtRequestDTO jwtRequest) {
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),
                jwtRequest.getPassword()));
        JwtResponseDTO jwtResponse =new JwtResponseDTO();
        jwtResponse.setToken(jwtUtil.generateToken(jwtRequest.getUsername()));
        return jwtResponse;
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String user() {
        return "Hello User!";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "Hello Admin!";
    }
}