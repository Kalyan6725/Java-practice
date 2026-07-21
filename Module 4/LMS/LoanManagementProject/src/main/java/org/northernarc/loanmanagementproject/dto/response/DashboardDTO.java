package org.northernarc.loanmanagementproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {
    private Long totalCustomers;
    private Long totalLoans;
    private Double totalLoanAmountDisbursed;
    private Double totalPenaltyCollected;
    private String topBranch;
    private String highestLoanCustomer;
}
