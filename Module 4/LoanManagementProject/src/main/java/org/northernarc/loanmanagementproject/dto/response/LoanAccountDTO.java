package org.northernarc.loanmanagementproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * LoanAccountDTO - Data Transfer Object for Loan Account
 * 
 * Provides safe exposure of loan account details without internal entity references.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanAccountDTO {
    
    private Long loanAccountId;
    
    private LocalDate loanStartDate;
    
    private LocalDate emiDueDate;
    
    private LocalDate loanCloseDate;
    
    private String status;
    
    private Double loanAmount;
    
    private Double emiAmount;
    
    private String customerName;
    
    private String loanProductName;
}
