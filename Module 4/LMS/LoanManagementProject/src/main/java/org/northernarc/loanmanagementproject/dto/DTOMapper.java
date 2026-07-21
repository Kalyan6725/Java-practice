package org.northernarc.loanmanagementproject.dto;

import org.northernarc.loanmanagementproject.entity.Customer;
import org.northernarc.loanmanagementproject.entity.EmiPayment;
import org.northernarc.loanmanagementproject.entity.LoanAccount;
import org.northernarc.loanmanagementproject.entity.LoanApplication;
import org.northernarc.loanmanagementproject.entity.LoanProduct;
import org.northernarc.loanmanagementproject.dto.response.CustomerResponse;
import org.northernarc.loanmanagementproject.dto.response.CustomerSummaryDTO;
import org.northernarc.loanmanagementproject.dto.response.EmiPaymentDTO;
import org.northernarc.loanmanagementproject.dto.response.EmiPaymentResponse;
import org.northernarc.loanmanagementproject.dto.response.LoanAccountDTO;
import org.northernarc.loanmanagementproject.dto.response.LoanAccountResponse;
import org.northernarc.loanmanagementproject.dto.response.LoanApplicationResponse;
import org.northernarc.loanmanagementproject.dto.response.LoanProductDTO;
import org.northernarc.loanmanagementproject.dto.response.LoanProductResponse;
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

    // ==================== FULL PRODUCTION RESPONSE MAPPERS ====================

    /**
     * Convert Customer entity to a safe CustomerResponse (never exposes password).
     */
    public CustomerResponse toCustomerResponse(Customer customer) {
        if (customer == null) {
            return null;
        }
        return CustomerResponse.builder()
                .customerId(customer.getCustomerId())
                .customerName(customer.getCustomerName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .branch(customer.getBranch())
                .role(customer.getRole())
                .status(customer.getStatus())
                .build();
    }

    /**
     * Convert LoanProduct entity to a full LoanProductResponse.
     */
    public LoanProductResponse toLoanProductResponse(LoanProduct product) {
        if (product == null) {
            return null;
        }
        return LoanProductResponse.builder()
                .loanCode(product.getLoanCode())
                .loanName(product.getLoanName())
                .loanType(product.getLoanType())
                .minimumAmount(product.getMinimumAmount())
                .maximumAmount(product.getMaximumAmount())
                .interestRate(product.getInterestRate())
                .minimumTenure(product.getMinimumTenure())
                .maximumTenure(product.getMaximumTenure())
                .processingFee(product.getProcessingFee())
                .dailyPenaltyRate(product.getDailyPenaltyRate())
                .active(product.getActive())
                .build();
    }

    /**
     * Convert LoanApplication entity to LoanApplicationResponse.
     */
    public LoanApplicationResponse toLoanApplicationResponse(LoanApplication application) {
        if (application == null) {
            return null;
        }
        Customer customer = application.getCustomer();
        LoanProduct product = application.getLoanProduct();
        return LoanApplicationResponse.builder()
                .applicationId(application.getApplicationId())
                .customerId(customer != null ? customer.getCustomerId() : null)
                .customerName(customer != null ? customer.getCustomerName() : null)
                .loanCode(product != null ? product.getLoanCode() : null)
                .loanName(product != null ? product.getLoanName() : null)
                .loanType(product != null ? product.getLoanType() : null)
                .requestedAmount(application.getRequestedAmount())
                .tenureMonths(application.getTenureMonths())
                .applicationDate(application.getApplicationDate())
                .status(application.getStatus())
                .remarks(application.getRemarks())
                .build();
    }

    /**
     * Convert LoanAccount entity to a full LoanAccountResponse, computing the
     * outstanding balance as loanAmount minus the sum of principal already paid.
     */
    public LoanAccountResponse toLoanAccountResponse(LoanAccount account) {
        if (account == null) {
            return null;
        }
        Customer customer = account.getCustomer();
        LoanProduct product = account.getLoanProduct();

        Double outstanding = account.getLoanAmount();
        if (account.getLoanAmount() != null && account.getEmiPayments() != null) {
            double principalPaid = account.getEmiPayments().stream()
                    .filter(p -> p.getPrincipalPaid() != null)
                    .mapToDouble(EmiPayment::getPrincipalPaid)
                    .sum();
            outstanding = account.getLoanAmount() - principalPaid;
        }

        return LoanAccountResponse.builder()
                .loanAccountId(account.getLoanAccountId())
                .loanNumber(account.getLoanNumber())
                .applicationId(account.getLoanApplication() != null
                        ? account.getLoanApplication().getApplicationId() : null)
                .customerId(customer != null ? customer.getCustomerId() : null)
                .customerName(customer != null ? customer.getCustomerName() : null)
                .loanCode(product != null ? product.getLoanCode() : null)
                .loanProductName(product != null ? product.getLoanName() : null)
                .loanAmount(account.getLoanAmount())
                .interestRate(account.getInterestRate())
                .tenureMonths(account.getTenureMonths())
                .emiAmount(account.getEmiAmount())
                .outstandingBalance(outstanding)
                .applicationDate(account.getApplicationDate())
                .approvalDate(account.getApprovalDate())
                .disbursementDate(account.getDisbursementDate())
                .loanStartDate(account.getLoanStartDate())
                .nextEmiDate(account.getEmiDueDate())
                .loanCloseDate(account.getLoanCloseDate())
                .status(account.getStatus())
                .build();
    }

    /**
     * Convert EmiPayment entity to a full EmiPaymentResponse (with breakdown).
     */
    public EmiPaymentResponse toEmiPaymentResponse(EmiPayment payment) {
        if (payment == null) {
            return null;
        }
        LoanAccount account = payment.getLoanAccount();
        return EmiPaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .loanAccountId(account != null ? account.getLoanAccountId() : null)
                .loanNumber(account != null ? account.getLoanNumber() : null)
                .installmentNo(payment.getInstallmentNo())
                .dueDate(payment.getDueDate())
                .paymentDate(payment.getPaymentDate())
                .emiAmount(payment.getEmiAmount())
                .principalPaid(payment.getPrincipalPaid())
                .interestPaid(payment.getInterestPaid())
                .penaltyPaid(payment.getPenaltyPaid())
                .totalPaid(payment.getAmountPaid())
                .paymentType(payment.getPaymentType())
                .status(payment.getStatus())
                .build();
    }
}
