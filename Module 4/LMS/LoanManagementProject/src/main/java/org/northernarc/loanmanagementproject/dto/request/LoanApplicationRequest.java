package org.northernarc.loanmanagementproject.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoanApplicationRequest - Payload for a customer applying for a loan.
 *
 * customerId is optional: for a USER it is resolved from the authenticated
 * principal and may be omitted; ADMIN/MANAGER may supply it to apply on behalf
 * of a customer. Business rules (amount/tenure within product limits, product
 * active, no duplicate open application) are enforced in the service layer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationRequest {

    private Long customerId;

    @NotBlank(message = "Loan code is required")
    @Size(min = 3, max = 20, message = "Loan code must be between 3 and 20 characters")
    private String loanCode;

    @NotNull(message = "Requested amount is required")
    @Positive(message = "Requested amount must be positive")
    @DecimalMax(value = "10000000.00", message = "Requested amount cannot exceed 10,000,000")
    private Double requestedAmount;

    @NotNull(message = "Tenure is required")
    @Min(value = 1, message = "Tenure must be at least 1 month")
    @Max(value = 600, message = "Tenure cannot exceed 600 months")
    private Integer tenureMonths;
}
