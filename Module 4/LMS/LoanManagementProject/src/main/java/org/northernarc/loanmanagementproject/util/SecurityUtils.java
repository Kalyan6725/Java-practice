package org.northernarc.loanmanagementproject.util;

import org.northernarc.loanmanagementproject.entity.Customer;
import org.northernarc.loanmanagementproject.exception.CustomerNotFoundException;
import org.northernarc.loanmanagementproject.repository.CustomerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * SecurityUtils - Resolves the currently authenticated customer and provides
 * ownership/role helpers used to enforce "a USER may only touch their own data".
 */
@Component
public class SecurityUtils {

    private final CustomerRepository customerRepository;

    public SecurityUtils(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * @return the email (username) of the authenticated principal, or null.
     */
    public String currentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        return auth.getName();
    }

    /**
     * @return the authenticated Customer entity.
     * @throws CustomerNotFoundException if no matching customer exists.
     */
    public Customer currentCustomer() {
        String email = currentEmail();
        if (email == null) {
            throw new CustomerNotFoundException("anonymous");
        }
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException(email));
    }

    public Long currentCustomerId() {
        return currentCustomer().getCustomerId();
    }

    public boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        String target = "ROLE_" + role;
        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (target.equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Staff = anyone who is not a plain USER (UNDERWRITER/MANAGER/ADMIN).
     */
    public boolean isStaff() {
        return hasRole("UNDERWRITER") || hasRole("MANAGER") || hasRole("ADMIN");
    }
}
