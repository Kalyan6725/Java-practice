package org.northernarc.loanmanagementproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoanProductResponse - Full public view of a loan product.
 * Used for product listing/detail pages (USER browsing and admin management).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanProductResponse {

    private String loanCode;
    private String loanName;
    private String loanType;
    private Double minimumAmount;
    private Double maximumAmount;
    private Double interestRate;
    private Integer minimumTenure;
    private Integer maximumTenure;
    private Double processingFee;
    private Double dailyPenaltyRate;
    private Boolean active;
}
