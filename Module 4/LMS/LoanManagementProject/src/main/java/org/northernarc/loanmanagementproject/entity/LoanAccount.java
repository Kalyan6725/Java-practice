package org.northernarc.loanmanagementproject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @NotBlank(message = "Loan number cannot be blank")
    @Size(min = 5, max = 30, message = "Loan number must be between 5 and 30 characters")
    @Column(name = "loan_number", nullable = false, unique = true, length = 30)
    private String loanNumber;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "application_id", unique = true)
    private LoanApplication loanApplication;

    @NotNull(message = "Application date cannot be null")
    @Column(name = "application_date", nullable = false)
    private LocalDate applicationDate;

    @Column(name = "approval_date")
    private LocalDate approvalDate;

    @Column(name = "disbursement_date")
    private LocalDate disbursementDate;

    @NotNull(message = "Loan start date cannot be null")
    @Column(name = "loan_start_date", nullable = false)
    private LocalDate loanStartDate;

    @Column(name = "next_emi_date")
    private LocalDate emiDueDate;

    @Column(name = "loan_close_date")
    private LocalDate loanCloseDate;

    @NotBlank(message = "Status cannot be blank")
    @Pattern(regexp = "APPROVED|DISBURSED|ACTIVE|CLOSED|OVERDUE",
             message = "Status must be one of: APPROVED, DISBURSED, ACTIVE, CLOSED, OVERDUE")
    @Column(name = "status", nullable = false)
    private String status;

    @NotNull(message = "Loan amount cannot be null")
    @Positive(message = "Loan amount must be positive")
    @DecimalMax(value = "10000000.00", message = "Loan amount cannot exceed 10,000,000")
    @Column(name = "loan_amount", nullable = false)
    private Double loanAmount;

    @NotNull(message = "Interest rate cannot be null")
    @PositiveOrZero(message = "Interest rate must be zero or positive")
    @DecimalMax(value = "100.00", message = "Interest rate cannot exceed 100%")
    @Column(name = "interest_rate", nullable = false)
    private Double interestRate = 10.0;

    @NotNull(message = "Tenure cannot be null")
    @Min(value = 1, message = "Tenure must be at least 1 month")
    @Max(value = 600, message = "Tenure cannot exceed 600 months")
    @Column(name = "tenure_months", nullable = false)
    private Integer tenureMonths = 12;

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

    @PrePersist
    void setDefaults() {
        if (loanNumber == null || loanNumber.isBlank()) {
            loanNumber = "LN-" + System.currentTimeMillis() + "-" + Math.abs(System.identityHashCode(this));
        }
        if (applicationDate == null) {
            applicationDate = loanStartDate != null ? loanStartDate : LocalDate.now();
        }
    }
}
