package org.northernarc.loanmanagementproject.dto;

import org.northernarc.loanmanagementproject.entity.Customer;
import org.northernarc.loanmanagementproject.entity.EmiPayment;
import org.northernarc.loanmanagementproject.entity.LoanAccount;
import org.northernarc.loanmanagementproject.entity.LoanProduct;
import org.northernarc.loanmanagementproject.dto.response.CustomerSummaryDTO;
import org.northernarc.loanmanagementproject.dto.response.EmiPaymentDTO;
import org.northernarc.loanmanagementproject.dto.response.LoanAccountDTO;
import org.northernarc.loanmanagementproject.dto.response.LoanProductDTO;
import org.springframework.stereotype.Component;

/**
 * DTOMapper - Utility class for converting entities to DTOs
 * 
 * Provides mapping methods to transform entity objects into their corresponding
 * Data Transfer Objects (DTOs). This prevents exposing internal entity structures
 * and provides a clean API contract.
 */
@Component
public class DTOMapper {
    
    /**
     * Convert Customer entity to CustomerSummaryDTO
     * Includes aggregated loan and penalty information
     */
    public CustomerSummaryDTO toCustomerSummaryDTO(Customer customer) {
        if (customer == null) {
            return null;
        }
        
        // Calculate derived fields
        Long numberOfLoans = customer.getLoanAccounts() != null ? 
            customer.getLoanAccounts().size() : 0L;
        
        Double totalLoanAmount = customer.getLoanAccounts() != null ?
            customer.getLoanAccounts().stream()
                .mapToDouble(LoanAccount::getLoanAmount)
                .sum() : 0.0;
        
        Double totalPenaltyPaid = customer.getLoanAccounts() != null ?
            customer.getLoanAccounts().stream()
                .flatMap(la -> la.getEmiPayments().stream())
                .mapToDouble(EmiPayment::getPenaltyPaid)
                .sum() : 0.0;
        
        return new CustomerSummaryDTO(
            customer.getCustomerName(),
            customer.getBranch(),
            numberOfLoans,
            totalLoanAmount,
            totalPenaltyPaid
        );
    }
    
    /**
     * Convert LoanProduct entity to LoanProductDTO
     */
    public LoanProductDTO toLoanProductDTO(LoanProduct loanProduct) {
        if (loanProduct == null) {
            return null;
        }
        
        return new LoanProductDTO(
            loanProduct.getLoanCode(),
            loanProduct.getLoanName(),
            loanProduct.getLoanType(),
            loanProduct.getDailyPenaltyRate()
        );
    }
    
    /**
     * Convert LoanAccount entity to LoanAccountDTO
     */
    public LoanAccountDTO toLoanAccountDTO(LoanAccount loanAccount) {
        if (loanAccount == null) {
            return null;
        }
        
        return new LoanAccountDTO(
            loanAccount.getLoanAccountId(),
            loanAccount.getLoanStartDate(),
            loanAccount.getEmiDueDate(),
            loanAccount.getLoanCloseDate(),
            loanAccount.getStatus(),
            loanAccount.getLoanAmount(),
            loanAccount.getEmiAmount(),
            loanAccount.getCustomer() != null ? loanAccount.getCustomer().getCustomerName() : null,
            loanAccount.getLoanProduct() != null ? loanAccount.getLoanProduct().getLoanName() : null
        );
    }
    
    /**
     * Convert EmiPayment entity to EmiPaymentDTO
     */
    public EmiPaymentDTO toEmiPaymentDTO(EmiPayment emiPayment) {
        if (emiPayment == null) {
            return null;
        }
        
        return new EmiPaymentDTO(
            emiPayment.getPaymentId(),
            emiPayment.getAmountPaid(),
            emiPayment.getPenaltyPaid(),
            emiPayment.getPaymentType(),
            emiPayment.getPaymentDate(),
            emiPayment.getLoanAccount() != null ? emiPayment.getLoanAccount().getLoanAccountId() : null
        );
    }
}
