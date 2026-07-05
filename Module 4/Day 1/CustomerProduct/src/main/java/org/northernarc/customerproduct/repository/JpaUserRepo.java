package org.northernarc.customerproduct.repository;

import org.northernarc.customerproduct.model.JpaUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepo extends JpaRepository<JpaUser, Long> {
    public JpaUser findByUsername(String username);
    Optional<JpaUser> findByUsernameIgnoreCase(String username);
    Optional<JpaUser> findByCustomerId(Integer customerId);
}
