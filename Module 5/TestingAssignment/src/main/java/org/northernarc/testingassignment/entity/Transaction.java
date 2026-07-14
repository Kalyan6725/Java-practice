package org.northernarc.testingassignment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @DecimalMin(value = "0", message = "Amount must be greater than zero")
    private Double amount;

    private LocalDateTime transactionDate;

    @Column(length = 500)
    private String description;

    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER_OUT, TRANSFER_IN
    }
}
