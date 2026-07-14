package org.northernarc.testingassignment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "accounts", uniqueConstraints = {@UniqueConstraint(columnNames = "accountNumber")})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Account number is required")
    @Column(unique = true)
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @DecimalMin(value = "0", message = "Opening balance cannot be negative")
    private Double balance;

    public enum AccountType {
        SAVINGS, CURRENT
    }
}
