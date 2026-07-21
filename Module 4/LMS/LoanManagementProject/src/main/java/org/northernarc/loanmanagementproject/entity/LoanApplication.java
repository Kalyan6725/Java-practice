package org.northernarc.loanmanagementproject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "loan_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long applicationId;

    @NotNull(message = "Customer cannot be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotNull(message = "Loan product cannot be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_code", nullable = false)
    private LoanProduct loanProduct;

    @NotNull(message = "Requested amount cannot be null")
    @Positive(message = "Requested amount must be positive")
    @DecimalMax(value = "10000000.00", message = "Requested amount cannot exceed 10,000,000")
    @Column(name = "requested_amount", nullable = false)
    private Double requestedAmount;

    @NotNull(message = "Tenure cannot be null")
    @Min(value = 1, message = "Tenure must be at least 1 month")
    @Max(value = 600, message = "Tenure cannot exceed 600 months")
    @Column(name = "tenure_months", nullable = false)
    private Integer tenureMonths;

    @NotNull(message = "Application date cannot be null")
    @Column(name = "application_date", nullable = false)
    private LocalDate applicationDate;

    @NotBlank(message = "Status cannot be blank")
    @Pattern(regexp = "SUBMITTED|UNDER_REVIEW|APPROVED|REJECTED",
            message = "Status must be one of: SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED")
    @Column(name = "status", nullable = false)
    private String status = "SUBMITTED";

    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    @Column(name = "remarks", length = 500)
    private String remarks;
}
