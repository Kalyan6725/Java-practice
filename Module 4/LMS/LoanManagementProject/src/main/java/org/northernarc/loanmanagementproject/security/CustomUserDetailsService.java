package org.northernarc.loanmanagementproject.security;

import org.northernarc.loanmanagementproject.entity.Customer;
import org.northernarc.loanmanagementproject.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Collections;

/**
 * CustomUserDetailsService - Implementation of Spring Security UserDetailsService
 * 
 * Loads user details from the database for authentication.
 * Assigns roles (ADMIN, MANAGER, USER) to users.
 * This service bridges the Customer entity with Spring Security's authentication mechanism.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    /**
     * Load user details by email (username)
     * Assigns role-based authority (ROLE_ADMIN, ROLE_MANAGER, ROLE_USER)
     * 
     * @param email the email address (used as username)
     * @return UserDetails object with customer information and role-based authorities
     * @throws UsernameNotFoundException if customer not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Customer not found with email: " + email));
        
        // Get role from customer and convert to Spring Security authority
        String role = customer.getRole();
        if (role == null || role.isEmpty()) {
            role = "USER";
        }
        
        // Create authority string with ROLE_ prefix
        String authority = "ROLE_" + role;
        
        return new org.springframework.security.core.userdetails.User(
            customer.getEmail(),
            customer.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority(authority))
        );
    }
}
