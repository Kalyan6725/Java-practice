package org.northernarc.testingassignment.service.impl;

import org.northernarc.testingassignment.entity.Account;
import org.northernarc.testingassignment.service.AccountService;

import java.util.List;
import java.util.Collections;

public class AccountServiceImpl implements AccountService {

    @Override
    public Account createAccount(String accountNumber, Long customerId, Account.AccountType accountType, Double balance) {
        return new Account();
    }

    @Override
    public Account getAccountById(Long id) {
        return new Account();
    }

    @Override
    public List<Account> getAccountsByCustomerId(Long customerId) {
        return Collections.emptyList();
    }

    @Override
    public Account updateAccount(Long id, Double balance) {
        return new Account();
    }

    @Override
    public void deleteAccount(Long id) {
    }

    @Override
    public List<Account> getAllAccounts() {
        return Collections.emptyList();
    }

    @Override
    public Account deposit(Long accountId, Double amount) {
        return new Account();
    }

    @Override
    public Account withdraw(Long accountId, Double amount) {
        return new Account();
    }

    @Override
    public void transfer(Long fromAccountId, Long toAccountId, Double amount) {
    }
}
