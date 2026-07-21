package org.northernarc.loanmanagementproject.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoanProductRequest - Payload used by ADMIN/MANAGER to create or update a loan product.
 * Mirrors the LoanProduct entity constraints without exposing JPA relationships.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanProductRequest {

    @NotBlank(message = "Loan code cannot be blank")
    @Size(min = 3, max = 20, message = "Loan code must be between 3 and 20 characters")
    private String loanCode;

    @NotBlank(message = "Loan name cannot be blank")
    @Size(min = 2, max = 100, message = "Loan name must be between 2 and 100 characters")
    private String loanName;

    @NotBlank(message = "Loan type cannot be blank")
    @Pattern(regexp = "PERSONAL|HOME|VEHICLE|EDUCATION|BUSINESS",
            message = "Loan type must be one of: PERSONAL, HOME, VEHICLE, EDUCATION, BUSINESS")
    private String loanType;

    @NotNull(message = "Minimum amount cannot be null")
    @Positive(message = "Minimum amount must be positive")
    @DecimalMax(value = "10000000.00", message = "Minimum amount cannot exceed 10,000,000")
    private Double minimumAmount;

    @NotNull(message = "Maximum amount cannot be null")
    @Positive(message = "Maximum amount must be positive")
    @DecimalMax(value = "10000000.00", message = "Maximum amount cannot exceed 10,000,000")
    private Double maximumAmount;

    @NotNull(message = "Interest rate cannot be null")
    @PositiveOrZero(message = "Interest rate must be zero or positive")
    @DecimalMax(value = "100.00", message = "Interest rate cannot exceed 100%")
    private Double interestRate;

    @NotNull(message = "Minimum tenure cannot be null")
    @Min(value = 1, message = "Minimum tenure must be at least 1 month")
    @Max(value = 600, message = "Minimum tenure cannot exceed 600 months")
    private Integer minimumTenure;

    @NotNull(message = "Maximum tenure cannot be null")
    @Min(value = 1, message = "Maximum tenure must be at least 1 month")
    @Max(value = 600, message = "Maximum tenure cannot exceed 600 months")
    private Integer maximumTenure;

    @NotNull(message = "Processing fee cannot be null")
    @PositiveOrZero(message = "Processing fee must be zero or positive")
    @DecimalMax(value = "1000000.00", message = "Processing fee cannot exceed 1,000,000")
    private Double processingFee;

    @PositiveOrZero(message = "Daily penalty rate must be zero or positive")
    @DecimalMax(value = "100.00", message = "Daily penalty rate cannot exceed 100%")
    private Double dailyPenaltyRate;

    private Boolean active = true;
}
