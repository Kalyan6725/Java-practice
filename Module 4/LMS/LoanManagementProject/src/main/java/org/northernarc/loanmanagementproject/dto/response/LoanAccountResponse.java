package org.northernarc.loanmanagementproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * LoanAccountResponse - Full view of a sanctioned loan account, including
 * schedule dates, financial terms and the outstanding balance. Replaces raw
 * LoanAccount entity exposure in API responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanAccountResponse {

    private Long loanAccountId;
    private String loanNumber;

    private Long applicationId;

    private Long customerId;
    private String customerName;

    private String loanCode;
    private String loanProductName;

    private Double loanAmount;
    private Double interestRate;
    private Integer tenureMonths;
    private Double emiAmount;
    private Double outstandingBalance;

    private LocalDate applicationDate;
    private LocalDate approvalDate;
    private LocalDate disbursementDate;
    private LocalDate loanStartDate;
    private LocalDate nextEmiDate;
    private LocalDate loanCloseDate;

    private String status;
}
