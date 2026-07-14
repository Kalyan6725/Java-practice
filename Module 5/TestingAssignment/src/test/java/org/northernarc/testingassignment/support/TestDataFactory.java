package org.northernarc.testingassignment.support;

import org.northernarc.testingassignment.dto.CustomerRequest;
import org.northernarc.testingassignment.entity.Customer;
import org.northernarc.testingassignment.entity.Account;
import org.northernarc.testingassignment.entity.Transaction;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static CustomerRequest customerRequest(String name, String email, String password, String phone) {
        return new CustomerRequest(name, email, password, phone);
    }

    public static Customer customer(Long id, String name, String email, String password, String phone) {
        return new Customer(id, name, email, password, phone);
    }

    public static Account account(Long id, String accountNumber, Customer customer, Account.AccountType accountType, Double balance) {
        Account account = new Account();
        account.setId(id);
        account.setAccountNumber(accountNumber);
        account.setCustomer(customer);
        account.setAccountType(accountType);
        account.setBalance(balance);
        return account;
    }

    public static Transaction transaction(Long id, Account account, Transaction.TransactionType transactionType, Double amount, java.time.LocalDateTime date, String description) {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setAccount(account);
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setTransactionDate(date);
        transaction.setDescription(description);
        return transaction;
    }
}
