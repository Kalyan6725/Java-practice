package org.northernarc.loanmanagementproject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loan_products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanProduct {
    @Id
    @NotBlank(message = "Loan code cannot be blank")
    @Size(min = 3, max = 20, message = "Loan code must be between 3 and 20 characters")
    @Column(name = "loan_code")
    private String loanCode;

    @NotBlank(message = "Loan name cannot be blank")
    @Size(min = 2, max = 100, message = "Loan name must be between 2 and 100 characters")
    @Column(name = "loan_name", nullable = false)
    private String loanName;

    @NotBlank(message = "Loan type cannot be blank")
    @Pattern(regexp = "PERSONAL|HOME|VEHICLE|EDUCATION|BUSINESS", 
             message = "Loan type must be one of: PERSONAL, HOME, VEHICLE, EDUCATION, BUSINESS")
    @Column(name = "loan_type", nullable = false)
    private String loanType;

    @NotNull(message = "Minimum amount cannot be null")
    @Positive(message = "Minimum amount must be positive")
    @DecimalMax(value = "10000000.00", message = "Minimum amount cannot exceed 10,000,000")
    @Column(name = "minimum_amount", nullable = false)
    private Double minimumAmount = 10000.0;

    @NotNull(message = "Maximum amount cannot be null")
    @Positive(message = "Maximum amount must be positive")
    @DecimalMax(value = "10000000.00", message = "Maximum amount cannot exceed 10,000,000")
    @Column(name = "maximum_amount", nullable = false)
    private Double maximumAmount = 1000000.0;

    @NotNull(message = "Interest rate cannot be null")
    @PositiveOrZero(message = "Interest rate must be zero or positive")
    @DecimalMax(value = "100.00", message = "Interest rate cannot exceed 100%")
    @Column(name = "interest_rate", nullable = false)
    private Double interestRate = 10.0;

    @NotNull(message = "Minimum tenure cannot be null")
    @Min(value = 1, message = "Minimum tenure must be at least 1 month")
    @Max(value = 600, message = "Minimum tenure cannot exceed 600 months")
    @Column(name = "minimum_tenure", nullable = false)
    private Integer minimumTenure = 12;

    @NotNull(message = "Maximum tenure cannot be null")
    @Min(value = 1, message = "Maximum tenure must be at least 1 month")
    @Max(value = 600, message = "Maximum tenure cannot exceed 600 months")
    @Column(name = "maximum_tenure", nullable = false)
    private Integer maximumTenure = 60;

    @NotNull(message = "Processing fee cannot be null")
    @PositiveOrZero(message = "Processing fee must be zero or positive")
    @DecimalMax(value = "1000000.00", message = "Processing fee cannot exceed 1,000,000")
    @Column(name = "processing_fee", nullable = false)
    private Double processingFee = 0.0;

    @PositiveOrZero(message = "Daily penalty rate must be zero or positive")
    @DecimalMax(value = "100.00", message = "Daily penalty rate cannot exceed 100%")
    @Column(name = "daily_penalty_rate")
    private Double dailyPenaltyRate;

    @NotNull(message = "Active flag cannot be null")
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "loanProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<LoanApplication> loanApplications = new ArrayList<>();

    @OneToMany(mappedBy = "loanProduct", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<LoanAccount> loanAccounts = new ArrayList<>();
}
