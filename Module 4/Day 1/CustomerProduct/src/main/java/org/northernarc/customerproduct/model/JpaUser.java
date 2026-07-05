package org.northernarc.customerproduct.model;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
