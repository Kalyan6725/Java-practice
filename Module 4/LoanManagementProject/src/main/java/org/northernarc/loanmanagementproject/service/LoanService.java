package org.northernarc.loanmanagementproject.service;

import org.northernarc.loanmanagementproject.entity.Customer;
import org.northernarc.loanmanagementproject.entity.LoanProduct;
import org.northernarc.loanmanagementproject.entity.LoanAccount;
import org.northernarc.loanmanagementproject.entity.EmiPayment;
import org.northernarc.loanmanagementproject.dto.response.DashboardDTO;
import org.northernarc.loanmanagementproject.exception.CustomerNotFoundException;
import org.northernarc.loanmanagementproject.exception.LoanProductNotFoundException;
import org.northernarc.loanmanagementproject.exception.LoanAccountNotFoundException;
import java.util.List;
import java.util.Optional;

public interface LoanService {
    // Customer operations
    Customer createCustomer(Customer customer);
    Customer getCustomerById(Long customerId) throws CustomerNotFoundException;
    List<Customer> getAllCustomers();
    Customer updateCustomer(Customer customer);
    void deleteCustomer(Long customerId);
    Optional<Customer> getCustomerByEmail(String email);

    // Loan Product operations
    LoanProduct createLoanProduct(LoanProduct loanProduct);
    LoanProduct getLoanProductByCode(String loanCode) throws LoanProductNotFoundException;
    List<LoanProduct> getAllLoanProducts();
    LoanProduct updateLoanProduct(LoanProduct loanProduct);
    void deleteLoanProduct(String loanCode);

    // Loan Account operations
    LoanAccount createLoanAccount(LoanAccount loanAccount);
    LoanAccount getLoanAccountById(Long loanAccountId) throws LoanAccountNotFoundException;
    List<LoanAccount> getAllLoanAccounts();
    List<LoanAccount> getLoanAccountsByCustomerId(Long customerId);
    List<LoanAccount> getLoanAccountsByStatus(String status);
    LoanAccount updateLoanAccount(LoanAccount loanAccount);
    void deleteLoanAccount(Long loanAccountId);

    // EMI Payment operations
    EmiPayment recordEmiPayment(EmiPayment emiPayment);
    EmiPayment getEmiPaymentById(Long paymentId);
    List<EmiPayment> getAllEmiPayments();
    List<EmiPayment> getEmiPaymentsByLoanAccountId(Long loanAccountId);
    List<EmiPayment> getEmiPaymentsByPaymentType(String paymentType);
    EmiPayment updateEmiPayment(EmiPayment emiPayment);
    void deleteEmiPayment(Long paymentId);
    
    // Final Challenge: Dashboard operations
    DashboardDTO getDashboardData();
}
