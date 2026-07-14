package org.northernarc.testingassignment.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.northernarc.testingassignment.repository.CustomerRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return customerRepository.findByEmail(email)
                .map(customer -> User.builder()
                        .username(customer.getEmail())
                        .password(customer.getPassword())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
