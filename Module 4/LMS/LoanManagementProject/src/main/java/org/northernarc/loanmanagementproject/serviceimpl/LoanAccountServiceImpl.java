package org.northernarc.loanmanagementproject.serviceimpl;

import org.northernarc.loanmanagementproject.entity.Customer;
import org.northernarc.loanmanagementproject.entity.LoanAccount;
import org.northernarc.loanmanagementproject.entity.LoanApplication;
import org.northernarc.loanmanagementproject.entity.LoanProduct;
import org.northernarc.loanmanagementproject.exception.LoanAccountNotFoundException;
import org.northernarc.loanmanagementproject.exception.ValidationException;
import org.northernarc.loanmanagementproject.repository.LoanAccountRepository;
import org.northernarc.loanmanagementproject.service.LoanAccountService;
import org.northernarc.loanmanagementproject.util.LoanCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Transactional
public class LoanAccountServiceImpl implements LoanAccountService {

    private final LoanAccountRepository loanAccountRepository;

    public LoanAccountServiceImpl(LoanAccountRepository loanAccountRepository) {
        this.loanAccountRepository = loanAccountRepository;
    }

    @Override
    public LoanAccount createFromApplication(LoanApplication application, Double approvedAmount) {
        Customer customer = application.getCustomer();
        LoanProduct product = application.getLoanProduct();

        double amount = approvedAmount != null ? approvedAmount : application.getRequestedAmount();
        if (amount <= 0) {
            throw new ValidationException("Approved amount must be positive");
        }
        if (product.getMinimumAmount() != null && amount < product.getMinimumAmount()) {
            throw new ValidationException("Approved amount is below the product minimum of " + product.getMinimumAmount());
        }
        if (product.getMaximumAmount() != null && amount > product.getMaximumAmount()) {
            throw new ValidationException("Approved amount exceeds the product maximum of " + product.getMaximumAmount());
        }

        double rate = product.getInterestRate() != null ? product.getInterestRate() : 0.0;
        int tenure = application.getTenureMonths();
        double emi = LoanCalculator.calculateEmi(amount, rate, tenure);

        LocalDate today = LocalDate.now();

        LoanAccount account = new LoanAccount();
        account.setLoanNumber(generateLoanNumber());
        account.setLoanApplication(application);
        account.setCustomer(customer);
        account.setLoanProduct(product);
        account.setLoanAmount(amount);
        account.setInterestRate(rate);
        account.setTenureMonths(tenure);
        account.setEmiAmount(emi);
        account.setApplicationDate(application.getApplicationDate() != null ? application.getApplicationDate() : today);
        account.setApprovalDate(today);
        // loanStartDate is mandatory; seeded now and finalised at disbursement.
        account.setLoanStartDate(today);
        account.setStatus("APPROVED");

        return loanAccountRepository.save(account);
    }

    @Override
    public LoanAccount disburse(Long loanAccountId) {
        LoanAccount account = getById(loanAccountId);
        if (!"APPROVED".equals(account.getStatus())) {
            throw new ValidationException("Only APPROVED loans can be disbursed. Current status: " + account.getStatus());
        }
        LocalDate today = LocalDate.now();
        account.setDisbursementDate(today);
        account.setLoanStartDate(today);
        account.setEmiDueDate(today.plusMonths(1));
        account.setStatus("ACTIVE");
        return loanAccountRepository.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public LoanAccount getById(Long loanAccountId) {
        return loanAccountRepository.findById(loanAccountId)
                .orElseThrow(() -> new LoanAccountNotFoundException(loanAccountId.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanAccount> getAll() {
        return loanAccountRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanAccount> getByCustomerId(Long customerId) {
        return loanAccountRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanAccount> getByStatus(String status) {
        return loanAccountRepository.findByStatus(status);
    }

    private String generateLoanNumber() {
        while (true) {
            String candidate = "LN-" + LocalDate.now().getYear() + "-"
                    + String.format("%08d", ThreadLocalRandom.current().nextInt(0, 100_000_000));
            boolean exists = loanAccountRepository.findAll().stream()
                    .anyMatch(a -> candidate.equals(a.getLoanNumber()));
            if (!exists) {
                return candidate;
            }
        }
    }
}
