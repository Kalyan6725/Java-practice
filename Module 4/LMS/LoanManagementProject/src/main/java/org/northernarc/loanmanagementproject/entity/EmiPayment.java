package org.northernarc.loanmanagementproject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "emi_payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmiPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @NotNull(message = "Installment number cannot be null")
    @Min(value = 1, message = "Installment number must be at least 1")
    @Column(name = "installment_no", nullable = false)
    private Integer installmentNo = 1;

    @NotNull(message = "Due date cannot be null")
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @NotNull(message = "EMI amount cannot be null")
    @PositiveOrZero(message = "EMI amount must be zero or positive")
    @DecimalMax(value = "1000000.00", message = "EMI amount cannot exceed 1,000,000")
    @Column(name = "emi_amount", nullable = false)
    private Double emiAmount = 0.0;

    @NotNull(message = "Principal paid cannot be null")
    @PositiveOrZero(message = "Principal paid must be zero or positive")
    @DecimalMax(value = "10000000.00", message = "Principal paid cannot exceed 10,000,000")
    @Column(name = "principal_paid", nullable = false)
    private Double principalPaid = 0.0;

    @NotNull(message = "Interest paid cannot be null")
    @PositiveOrZero(message = "Interest paid must be zero or positive")
    @DecimalMax(value = "10000000.00", message = "Interest paid cannot exceed 10,000,000")
    @Column(name = "interest_paid", nullable = false)
    private Double interestPaid = 0.0;

    @NotNull(message = "Amount paid cannot be null")
    @Positive(message = "Amount paid must be positive")
    @DecimalMax(value = "10000000.00", message = "Amount paid cannot exceed 10,000,000")
    @Column(name = "total_paid", nullable = false)
    private Double amountPaid;

    @PositiveOrZero(message = "Penalty paid must be zero or positive")
    @DecimalMax(value = "1000000.00", message = "Penalty paid cannot exceed 1,000,000")
    @Column(name = "penalty_paid")
    private Double penaltyPaid;

    @NotBlank(message = "Payment type cannot be blank")
    @Pattern(regexp = "CASH|CARD|ONLINE|UPI", 
             message = "Payment type must be one of: CASH, CARD, ONLINE, UPI")
    @Column(name = "payment_type", nullable = false)
    private String paymentType;

    @NotNull(message = "Payment date cannot be null")
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @NotBlank(message = "Status cannot be blank")
    @Pattern(regexp = "PENDING|PAID|OVERDUE", message = "Status must be one of: PENDING, PAID, OVERDUE")
    @Column(name = "status", nullable = false)
    private String status = "PAID";

    @NotNull(message = "Loan account cannot be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_account_id", nullable = false)
    private LoanAccount loanAccount;

    @PrePersist
    void setDefaults() {
        if (dueDate == null) {
            dueDate = paymentDate != null ? paymentDate : LocalDate.now();
        }
    }
}
