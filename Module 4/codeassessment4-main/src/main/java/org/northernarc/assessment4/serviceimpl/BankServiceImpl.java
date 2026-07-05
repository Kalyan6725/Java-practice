package org.northernarc.assessment4.serviceimpl;

import org.northernarc.assessment4.dto.CustomerSummaryDTO;
import org.northernarc.assessment4.dto.DashboardResponse;
import org.northernarc.assessment4.exceptions.AccountNotFoundException;
import org.northernarc.assessment4.exceptions.CustomerNotFoundException;
import org.northernarc.assessment4.exceptions.DuplicateEmailException;
import org.northernarc.assessment4.model.Account;
import org.northernarc.assessment4.model.Customer;
import org.northernarc.assessment4.model.Transaction;
import org.northernarc.assessment4.repository.AccountRepository;
import org.northernarc.assessment4.repository.CustomerRepository;
import org.northernarc.assessment4.repository.TransactionRepository;
import org.northernarc.assessment4.service.BankService;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class BankServiceImpl implements BankService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    // --- Core Entity Writing Persistence Methods ---
    @Override
    @Transactional
    public Customer saveCustomer(Customer customer) {
        customerRepository.findByEmail(customer.getEmail())
                .filter(existing -> customer.getCustomerId() == null || !existing.getCustomerId().equals(customer.getCustomerId()))
                .ifPresent(existing -> {
                    throw new DuplicateEmailException("Email already exists: " + customer.getEmail());
                });

        if (customer.getPassword() != null && !customer.getPassword().startsWith("$2a$")) {
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        }
        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    @Transactional
    public void deleteAccount(String accountNumber) {
        if (!accountRepository.existsById(accountNumber)) {
            throw new AccountNotFoundException("Account not found with number: " + accountNumber);
        }
        accountRepository.deleteById(accountNumber);
    }

    @Override
    public List<Account> getAccountsByType(String accountType) {
        return accountRepository.findByAccountType(accountType);
    }

    @Override
    public List<Customer> getCustomersByBranch(String branch) {
        return customerRepository.findByBranch(branch);
    }

    @Override
    public List<Transaction> getTransactionsByType(String transactionType) {
        return transactionRepository.findByTransactionType(transactionType);
    }

    @Override
    public List<Account> getAccountsWithBalanceGreaterThan(double amount) {
        return accountRepository.findByBalanceGreaterThan(amount);
    }

    @Override
    public List<Customer> getRichCustomers(double threshold) {
        return customerRepository.findRichCustomers(threshold);
    }

    @Override
    public Map<String, Double> getTotalBalancePerBranch() {
        List<Object[]> rows = customerRepository.findTotalBalancePerBranch();
        Map<String, Double> balancesByBranch = new LinkedHashMap<>();
        for (Object[] row : rows) {
            String branch = (String) row[0];
            Double total = (Double) row[1];
            balancesByBranch.put(branch, total);
        }
        return balancesByBranch;
    }

    @Override
    public List<Customer> getCustomersWithMultipleAccounts() {
        return customerRepository.findCustomersWithMultipleAccounts();
    }

    @Override
    public Transaction getLatestTransaction() {
        List<Transaction> latest = transactionRepository.findLatestTransaction(PageRequest.of(0, 1));
        return latest.isEmpty() ? null : latest.get(0);
    }

    @Override
    public List<Account> getAccountsWithNoTransactions() {
        return accountRepository.findAccountsWithNoTransactions();
    }

    @Override
    @Transactional
    public void increaseAccountBalance(String accountNumber, double amount) {
        int updatedRows = accountRepository.increaseBalance(accountNumber, amount);
        if (updatedRows == 0) {
            throw new AccountNotFoundException("Account not found with number: " + accountNumber);
        }
    }

    @Override
    public Page<Account> getAllAccountsPaginated(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Override
    public CustomerSummaryDTO getCustomerSummary(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));

        long accountCount = accountRepository.countByCustomerCustomerId(customerId);
        double totalBalance = accountRepository.sumBalanceByCustomerId(customerId);

        return new CustomerSummaryDTO(
                customer.getCustomerName(),
                customer.getBranch(),
                accountCount,
                totalBalance
        );
    }

    @Override
    public DashboardResponse getDashboardMetrics() {
        List<Object[]> rows = accountRepository.fetchDashboardMetrics();
        Object[] row = rows.isEmpty() ? new Object[]{0L, 0L, 0.0, null, null} : rows.get(0);
        Long totalCustomers = ((Number) row[0]).longValue();
        Long totalAccounts = ((Number) row[1]).longValue();
        Double totalBalance = ((Number) row[2]).doubleValue();
        String topBranch = (String) row[3];
        String highestBalanceCustomer = (String) row[4];

        return new DashboardResponse(totalCustomers, totalAccounts, totalBalance, topBranch, highestBalanceCustomer);
    }

}
