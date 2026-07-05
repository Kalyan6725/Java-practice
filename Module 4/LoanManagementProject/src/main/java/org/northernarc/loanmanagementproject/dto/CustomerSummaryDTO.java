package org.northernarc.loanmanagementproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CustomerSummaryDTO - Data Transfer Object for Customer Summary
 * 
 * This DTO projects customer information with aggregated loan and penalty details.
 * Used to prevent exposing internal entity structure and to provide focused data views.
 * 
 * Fields:
 * - customerName: Full name of the customer
 * - branch: Branch/location of the customer
 * - numberOfLoans: Count of active/total loan accounts
 * - totalLoanAmount: Sum of all loan amounts
 * - totalPenaltyPaid: Sum of all penalties paid
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSummaryDTO {
    
    private String customerName;
    
    private String branch;
    
    private Long numberOfLoans;
    
    private Double totalLoanAmount;
    
    private Double totalPenaltyPaid;
}
