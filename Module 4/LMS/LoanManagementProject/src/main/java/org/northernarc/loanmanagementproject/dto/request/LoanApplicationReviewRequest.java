package org.northernarc.loanmanagementproject.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoanApplicationReviewRequest - Underwriter decision on a loan application.
 *
 * decision drives the workflow transition:
 * - APPROVED  -> application APPROVED and a LoanAccount is created.
 * - REJECTED  -> application REJECTED (remarks strongly recommended).
 * - UNDER_REVIEW -> mark the application as being reviewed.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationReviewRequest {

    @NotBlank(message = "Decision is required")
    @Pattern(regexp = "UNDER_REVIEW|APPROVED|REJECTED",
            message = "Decision must be one of: UNDER_REVIEW, APPROVED, REJECTED")
    private String decision;

    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks;

    @Positive(message = "Approved amount must be positive")
    @DecimalMax(value = "10000000.00", message = "Approved amount cannot exceed 10,000,000")
    private Double approvedAmount;
}
