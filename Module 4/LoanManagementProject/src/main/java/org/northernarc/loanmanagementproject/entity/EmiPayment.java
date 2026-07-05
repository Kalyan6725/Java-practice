package org.northernarc.loanmanagementproject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.northernarc.loanmanagementproject.entity.LoanAccount;

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

    @NotNull(message = "Amount paid cannot be null")
    @Positive(message = "Amount paid must be positive")
    @DecimalMax(value = "10000000.00", message = "Amount paid cannot exceed 10,000,000")
    @Column(name = "amount_paid", nullable = false)
    private Double amountPaid;

    @PositiveOrZero(message = "Penalty paid must be zero or positive")
    @DecimalMax(value = "1000000.00", message = "Penalty paid cannot exceed 1,000,000")
    @Column(name = "penalty_paid")
    private Double penaltyPaid;

    @NotBlank(message = "Payment type cannot be blank")
    @Pattern(regexp = "CASH|CARD|ONLINE|UPI", 
             message = "Payment type must be one of: CASH, CARD, ONLINE, UPI")
    @Column(name = "payment_type")
    private String paymentType;

    @NotNull(message = "Payment date cannot be null")
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @NotNull(message = "Loan account cannot be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_account_id", nullable = false)
    private LoanAccount loanAccount;
}
