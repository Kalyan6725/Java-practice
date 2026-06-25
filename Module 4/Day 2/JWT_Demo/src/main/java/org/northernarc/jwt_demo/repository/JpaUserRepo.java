package org.northernarc.jwt_demo.repository;

import org.northernarc.jwt_demo.model.JpaUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepo extends JpaRepository<JpaUser, Long> {
    public JpaUser findByUsername(String username);
}
