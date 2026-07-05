package org.northernarc.loanmanagementproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * EmiPaymentDTO - Data Transfer Object for EMI Payment
 * 
 * Safe DTO for payment information without exposing entity details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmiPaymentDTO {
    
    private Long paymentId;
    
    private Double amountPaid;
    
    private Double penaltyPaid;
    
    private String paymentType;
    
    private LocalDate paymentDate;
    
    private Long loanAccountId;
}
