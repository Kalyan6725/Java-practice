package org.northernarc.loanmanagementproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * LoanApplicationResponse - View of a customer's loan application and its
 * current workflow state (SUBMITTED / UNDER_REVIEW / APPROVED / REJECTED).
 * Flattens the customer and product references to safe display fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanApplicationResponse {

    private Long applicationId;

    private Long customerId;
    private String customerName;

    private String loanCode;
    private String loanName;
    private String loanType;

    private Double requestedAmount;
    private Integer tenureMonths;
    private LocalDate applicationDate;
    private String status;
    private String remarks;

    // Populated once an application has been approved into an account.
    private Long loanAccountId;
}
