package org.northernarc.loanmanagementproject.entity;
import org.northernarc.loanmanagementproject.entity.EmiPayment;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loan_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_account_id")
    private Long loanAccountId;

    @NotNull(message = "Loan start date cannot be null")
    @Column(name = "loan_start_date", nullable = false)
    private LocalDate loanStartDate;

    @Column(name = "emi_due_date")
    private LocalDate emiDueDate;

    @Column(name = "loan_close_date")
    private LocalDate loanCloseDate;

    @NotBlank(message = "Status cannot be blank")
    @Pattern(regexp = "ACTIVE|CLOSED|OVERDUE", 
             message = "Status must be one of: ACTIVE, CLOSED, OVERDUE")
    @Column(name = "status", nullable = false)
    private String status;

    @NotNull(message = "Loan amount cannot be null")
    @Positive(message = "Loan amount must be positive")
    @DecimalMax(value = "10000000.00", message = "Loan amount cannot exceed 10,000,000")
    @Column(name = "loan_amount", nullable = false)
    private Double loanAmount;

    @PositiveOrZero(message = "EMI amount must be zero or positive")
    @DecimalMax(value = "1000000.00", message = "EMI amount cannot exceed 1,000,000")
    @Column(name = "emi_amount")
    private Double emiAmount;

    @NotNull(message = "Customer cannot be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotNull(message = "Loan product cannot be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_code", nullable = false)
    private LoanProduct loanProduct;

    @OneToMany(mappedBy = "loanAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<EmiPayment> emiPayments = new ArrayList<>();
}
