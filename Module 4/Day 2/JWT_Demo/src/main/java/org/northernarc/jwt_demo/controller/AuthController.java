package org.northernarc.jwt_demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String welcome() {
        return "Welcome to the Spring Boot Security Demo! USER and ADMIN has Access";
    }
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userLogin() {
        return "User Login";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminLogin() {
        return "Admin Login";
    }
}
