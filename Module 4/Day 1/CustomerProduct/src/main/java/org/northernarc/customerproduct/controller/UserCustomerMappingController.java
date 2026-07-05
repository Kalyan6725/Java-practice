package org.northernarc.customerproduct.controller;

import org.northernarc.customerproduct.exceptions.CustomerNotFound;
import org.northernarc.customerproduct.model.Customer;
import org.northernarc.customerproduct.model.JpaUser;
import org.northernarc.customerproduct.repository.CustomerRepository;
import org.northernarc.customerproduct.repository.JpaUserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/admin/user-customer")
@PreAuthorize("hasRole('ADMIN')")
public class UserCustomerMappingController {

    private final JpaUserRepo jpaUserRepo;
    private final CustomerRepository customerRepository;

    public UserCustomerMappingController(JpaUserRepo jpaUserRepo, CustomerRepository customerRepository) {
        this.jpaUserRepo = jpaUserRepo;
        this.customerRepository = customerRepository;
    }

    @PutMapping("/{username}/{customerId}")
    public ResponseEntity<Map<String, String>> mapUserToCustomer(
            @PathVariable String username,
            @PathVariable Integer customerId) {
        JpaUser user = jpaUserRepo.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found: " + username));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFound("no customer found with id " + customerId));

        jpaUserRepo.findByCustomerId(customerId)
                .filter(existingUser -> !existingUser.getUsername().equalsIgnoreCase(username))
                .ifPresent(existingUser -> {
                    existingUser.setCustomer(null);
                    jpaUserRepo.save(existingUser);
                });

        user.setCustomer(customer);
        jpaUserRepo.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User " + username + " mapped to customer " + customerId);
        return ResponseEntity.ok(response);
    }
}
