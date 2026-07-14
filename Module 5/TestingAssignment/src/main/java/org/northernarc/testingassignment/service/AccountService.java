package org.northernarc.testingassignment.service;

import org.northernarc.testingassignment.entity.Account;
import java.util.List;

public interface AccountService {
    Account createAccount(String accountNumber, Long customerId, Account.AccountType accountType, Double balance);
    Account getAccountById(Long id);
    List<Account> getAccountsByCustomerId(Long customerId);
    Account updateAccount(Long id, Double balance);
    void deleteAccount(Long id);
    List<Account> getAllAccounts();
    Account deposit(Long accountId, Double amount);
    Account withdraw(Long accountId, Double amount);
    void transfer(Long fromAccountId, Long toAccountId, Double amount);
}
