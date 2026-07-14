package org.northernarc.testingassignment.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.northernarc.testingassignment.entity.Account;
import org.northernarc.testingassignment.entity.Customer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer testCustomer;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer(null, "John Doe", "john@test.com", "pass123", "9876543210");
        Customer saved = customerRepository.saveAndFlush(testCustomer);
        testAccount = new Account(null, "ACC001", saved, Account.AccountType.SAVINGS, 5000.0);
    }

    @Test
    void save_whenAccountIsValid_persistsAccount() {
        // Arrange & Act
        Account saved = accountRepository.saveAndFlush(testAccount);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("ACC001", saved.getAccountNumber());
        assertEquals(5000.0, saved.getBalance());
    }

    @Test
    void findByAccountNumber_whenAccountExists_returnsAccount() {
        // Arrange
        accountRepository.saveAndFlush(testAccount);

        // Act
        var result = accountRepository.findByAccountNumber("ACC001");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(5000.0, result.get().getBalance());
    }

    @Test
    void existsByAccountNumber_whenAccountExists_returnsTrue() {
        // Arrange
        accountRepository.saveAndFlush(testAccount);

        // Act & Assert
        assertTrue(accountRepository.existsByAccountNumber("ACC001"));
        assertFalse(accountRepository.existsByAccountNumber("ACC999"));
    }

    @Test
    void save_whenAccountNumberIsDuplicated_throwsDataIntegrityViolationException() {
        // Arrange
        accountRepository.saveAndFlush(testAccount);
        Account duplicate = new Account(null, "ACC001", testCustomer, Account.AccountType.CURRENT, 1000.0);

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> 
            accountRepository.saveAndFlush(duplicate)
        );
    }

    @Test
    void findByCustomerId_whenCustomerHasAccounts_returnsAllCustomerAccounts() {
        // Arrange
        Account saved1 = accountRepository.saveAndFlush(testAccount);
        Account account2 = new Account(null, "ACC002", testCustomer, Account.AccountType.CURRENT, 2000.0);
        accountRepository.saveAndFlush(account2);

        // Act
        List<Account> results = accountRepository.findByCustomerId(testCustomer.getId());

        // Assert
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(a -> a.getAccountNumber().equals("ACC001")));
        assertTrue(results.stream().anyMatch(a -> a.getAccountNumber().equals("ACC002")));
    }

    @Test
    void findByCustomerId_whenCustomerHasNoAccounts_returnsEmptyList() {
        // Arrange
        Customer newCustomer = new Customer(null, "Jane Doe", "jane@test.com", "pass456", "1234567890");
        Customer saved = customerRepository.saveAndFlush(newCustomer);

        // Act
        List<Account> results = accountRepository.findByCustomerId(saved.getId());

        // Assert
        assertTrue(results.isEmpty());
    }

    @Test
    void delete_whenAccountExists_deletesSuccessfully() {
        // Arrange
        Account saved = accountRepository.saveAndFlush(testAccount);

        // Act
        accountRepository.deleteById(saved.getId());

        // Assert
        assertFalse(accountRepository.findById(saved.getId()).isPresent());
    }

    @Test
    void save_whenBalanceIsNegative_throwsConstraintViolationException() {
        // Arrange
        Account invalidAccount = new Account(null, "ACC003", testCustomer, Account.AccountType.SAVINGS, -1000.0);

        // Act & Assert
        assertThrows(Exception.class, () -> 
            accountRepository.saveAndFlush(invalidAccount)
        );
    }
}
