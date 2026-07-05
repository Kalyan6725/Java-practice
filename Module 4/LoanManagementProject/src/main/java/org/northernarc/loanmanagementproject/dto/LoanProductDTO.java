package org.northernarc.loanmanagementproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * LoanProductDTO - Data Transfer Object for Loan Product
 * 
 * Exposes only essential loan product information.
 * Hides internal implementation details and validation annotations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanProductDTO {
    
    private String loanCode;
    
    private String loanName;
    
    private String loanType;
    
    private Double dailyPenaltyRate;
}
