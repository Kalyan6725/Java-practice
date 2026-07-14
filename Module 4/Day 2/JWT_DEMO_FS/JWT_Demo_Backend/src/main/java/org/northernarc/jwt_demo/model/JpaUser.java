package org.northernarc.jwt_demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JpaUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // String email;
    @Column(unique = true)
    String username;
    String password;
    String role;
}
