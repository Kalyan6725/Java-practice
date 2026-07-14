package org.northernarc.loanmanagementproject.serviceimpl;

import org.northernarc.loanmanagementproject.entity.LoanAccount;
import org.northernarc.loanmanagementproject.repository.CustomerRepository;
import org.northernarc.loanmanagementproject.repository.EmiPaymentRepository;
import org.northernarc.loanmanagementproject.repository.LoanAccountRepository;
import org.northernarc.loanmanagementproject.repository.LoanProductRepository;
import org.northernarc.loanmanagementproject.service.LoanService;
import org.northernarc.loanmanagementproject.entity.Customer;
import org.northernarc.loanmanagementproject.entity.LoanProduct;
import org.northernarc.loanmanagementproject.entity.EmiPayment;
import org.northernarc.loanmanagementproject.dto.response.DashboardDTO;
import org.northernarc.loanmanagementproject.exception.CustomerNotFoundException;
import org.northernarc.loanmanagementproject.exception.LoanProductNotFoundException;
import org.northernarc.loanmanagementproject.exception.LoanAccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanProductRepository loanProductRepository;

    @Autowired
    private LoanAccountRepository loanAccountRepository;

    @Autowired
    private EmiPaymentRepository emiPaymentRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId.toString()));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        if (customer.getCustomerId() != null) {
            customerRepository.findById(customer.getCustomerId())
                    .orElseThrow(() -> new CustomerNotFoundException(customer.getCustomerId().toString()));
        }
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId.toString()));
        customerRepository.deleteById(customerId);
    }

    @Override
    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public LoanProduct createLoanProduct(LoanProduct loanProduct) {
        return loanProductRepository.save(loanProduct);
    }

    @Override
    public LoanProduct getLoanProductByCode(String loanCode) {
        return loanProductRepository.findById(loanCode)
                .orElseThrow(() -> new LoanProductNotFoundException(loanCode));
    }

    @Override
    public List<LoanProduct> getAllLoanProducts() {
        return loanProductRepository.findAll();
    }

    @Override
    public LoanProduct updateLoanProduct(LoanProduct loanProduct) {
        if (loanProduct.getLoanCode() != null) {
            loanProductRepository.findById(loanProduct.getLoanCode())
                    .orElseThrow(() -> new LoanProductNotFoundException(loanProduct.getLoanCode()));
        }
        return loanProductRepository.save(loanProduct);
    }

    @Override
    public void deleteLoanProduct(String loanCode) {
        loanProductRepository.findById(loanCode)
                .orElseThrow(() -> new LoanProductNotFoundException(loanCode));
        loanProductRepository.deleteById(loanCode);
    }

    @Override
    public LoanAccount createLoanAccount(LoanAccount loanAccount) {
        return loanAccountRepository.save(loanAccount);
    }

    @Override
    public LoanAccount getLoanAccountById(Long loanAccountId) {
        return loanAccountRepository.findById(loanAccountId)
                .orElseThrow(() -> new LoanAccountNotFoundException(loanAccountId.toString()));
    }

    @Override
    public List<LoanAccount> getAllLoanAccounts() {
        return loanAccountRepository.findAll();
    }

    @Override
    public List<LoanAccount> getLoanAccountsByCustomerId(Long customerId) {
        return loanAccountRepository.findByCustomerId(customerId);
    }

    @Override
    public List<LoanAccount> getLoanAccountsByStatus(String status) {
        return loanAccountRepository.findByStatus(status);
    }

    @Override
    public LoanAccount updateLoanAccount(LoanAccount loanAccount) {
        if (loanAccount.getLoanAccountId() != null) {
            loanAccountRepository.findById(loanAccount.getLoanAccountId())
                    .orElseThrow(() -> new LoanAccountNotFoundException(loanAccount.getLoanAccountId().toString()));
        }
        return loanAccountRepository.save(loanAccount);
    }

    @Override
    public void deleteLoanAccount(Long loanAccountId) {
        loanAccountRepository.findById(loanAccountId)
                .orElseThrow(() -> new LoanAccountNotFoundException(loanAccountId.toString()));
        loanAccountRepository.deleteById(loanAccountId);
    }

    @Override
    public EmiPayment recordEmiPayment(EmiPayment emiPayment) {
        return emiPaymentRepository.save(emiPayment);
    }

    @Override
    public EmiPayment getEmiPaymentById(Long paymentId) {
        return emiPaymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("EMI Payment not found with ID: " + paymentId));
    }

    @Override
    public List<EmiPayment> getAllEmiPayments() {
        return emiPaymentRepository.findAll();
    }

    @Override
    public List<EmiPayment> getEmiPaymentsByLoanAccountId(Long loanAccountId) {
        return emiPaymentRepository.findByLoanAccountId(loanAccountId);
    }

    @Override
    public List<EmiPayment> getEmiPaymentsByPaymentType(String paymentType) {
        return emiPaymentRepository.findByPaymentType(paymentType);
    }

    @Override
    public EmiPayment updateEmiPayment(EmiPayment emiPayment) {
        return emiPaymentRepository.save(emiPayment);
    }

    @Override
    public void deleteEmiPayment(Long paymentId) {
        emiPaymentRepository.deleteById(paymentId);
    }

    // ==================== FINAL CHALLENGE: DASHBOARD ====================
    
    @Override
    public DashboardDTO getDashboardData() {
        Long totalCustomers = customerRepository.getTotalCustomerCount();
        Long totalLoans = loanAccountRepository.getTotalLoanCount();
        Double totalLoanAmount = loanAccountRepository.getTotalLoanAmountDisbursed();
        Double totalPenalty = emiPaymentRepository.getTotalPenaltyCollected();
        String topBranch = customerRepository.getTopBranch();
        String highestLoanCustomer = customerRepository.getHighestLoanCustomer();
        
        return DashboardDTO.builder()
                .totalCustomers(totalCustomers != null ? totalCustomers : 0L)
                .totalLoans(totalLoans != null ? totalLoans : 0L)
                .totalLoanAmountDisbursed(totalLoanAmount != null ? totalLoanAmount : 0.0)
                .totalPenaltyCollected(totalPenalty != null ? totalPenalty : 0.0)
                .topBranch(topBranch != null ? topBranch : "N/A")
                .highestLoanCustomer(highestLoanCustomer != null ? highestLoanCustomer : "N/A")
                .build();
    }
}
