package org.northernarc.testingassignment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.northernarc.testingassignment.entity.Account;
import org.northernarc.testingassignment.entity.Customer;
import org.northernarc.testingassignment.exception.AccountNotFoundException;
import org.northernarc.testingassignment.exception.CustomerNotFoundException;
import org.northernarc.testingassignment.exception.InsufficientBalanceException;
import org.northernarc.testingassignment.repository.AccountRepository;
import org.northernarc.testingassignment.repository.CustomerRepository;
import org.northernarc.testingassignment.service.impl.AccountServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Customer testCustomer;
    private Account testAccount;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer(1L, "John Doe", "john@example.com", "pass", "9876543210");
        testAccount = new Account(1L, "ACC001", testCustomer, Account.AccountType.SAVINGS, 5000.0);
    }

    @Test
    void createAccount_whenCustomerExistsAndBalanceIsValid_createsAccount() {
        // Arrange
        Account newAccount = new Account(null, "ACC002", testCustomer, Account.AccountType.CURRENT, 1000.0);
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));
        when(accountRepository.existsByAccountNumber("ACC002")).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // Act
        Account result = accountService.createAccount("ACC001", 1L, Account.AccountType.SAVINGS, 1000.0);

        // Assert
        assertNotNull(result);
        assertEquals("ACC001", result.getAccountNumber());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void createAccount_whenCustomerNotFound_throwsCustomerNotFoundException() {
        // Arrange
        when(customerRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(CustomerNotFoundException.class, 
            () -> accountService.createAccount("ACC002", 999L, Account.AccountType.SAVINGS, 1000.0));
    }

    @Test
    void createAccount_whenBalanceIsNegative_throwsIllegalArgumentException() {
        // Arrange
        when(customerRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> accountService.createAccount("ACC002", 1L, Account.AccountType.SAVINGS, -100.0));
    }

    @Test
    void createAccount_whenAccountNumberAlreadyExists_throwsIllegalArgumentException() {
        // Arrange
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(accountRepository.existsByAccountNumber("ACC002")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> accountService.createAccount("ACC002", 1L, Account.AccountType.SAVINGS, 1000.0));
    }

    @Test
    void deposit_whenAmountIsPositiveAndAccountExists_increasesBalance() {
        // Arrange
        double depositAmount = 500.0;
        Account updatedAccount = new Account(1L, "ACC001", testCustomer, Account.AccountType.SAVINGS, 5500.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);

        // Act
        Account result = accountService.deposit(1L, depositAmount);

        // Assert
        assertEquals(5500.0, result.getBalance());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void deposit_whenAmountIsZeroOrNegative_throwsIllegalArgumentException() {
        // Arrange
        lenient().when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> accountService.deposit(1L, -100.0));
        assertThrows(IllegalArgumentException.class, () -> accountService.deposit(1L, 0.0));
    }

    @Test
    void withdraw_whenAmountIsValidAndBalanceSufficient_decreasesBalance() {
        // Arrange
        double withdrawAmount = 1000.0;
        Account updatedAccount = new Account(1L, "ACC001", testCustomer, Account.AccountType.SAVINGS, 4000.0);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);

        // Act
        Account result = accountService.withdraw(1L, withdrawAmount);

        // Assert
        assertEquals(4000.0, result.getBalance());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void withdraw_whenAmountExceedsBalance_throwsInsufficientBalanceException() {
        // Arrange
        double withdrawAmount = 6000.0;
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () -> accountService.withdraw(1L, withdrawAmount));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void withdraw_whenAmountIsZeroOrNegative_throwsIllegalArgumentException() {
        // Arrange
        lenient().when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> accountService.withdraw(1L, -100.0));
        assertThrows(IllegalArgumentException.class, () -> accountService.withdraw(1L, 0.0));
    }

    @Test
    void getAccountById_whenAccountExists_returnsAccount() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        // Act
        Account result = accountService.getAccountById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("ACC001", result.getAccountNumber());
    }

    @Test
    void getAccountById_whenAccountNotFound_throwsAccountNotFoundException() {
        // Arrange
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(999L));
    }

    @Test
    void getAllAccounts_returnsAllAccounts() {
        // Arrange
        List<Account> accounts = List.of(testAccount, new Account(2L, "ACC002", testCustomer, Account.AccountType.CURRENT, 2000.0));
        when(accountRepository.findAll()).thenReturn(accounts);

        // Act
        List<Account> result = accountService.getAllAccounts();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getAccountsByCustomerId_returnsCustomerAccounts() {
        // Arrange
        List<Account> accounts = List.of(testAccount);
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(accountRepository.findByCustomerId(1L)).thenReturn(accounts);

        // Act
        List<Account> result = accountService.getAccountsByCustomerId(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals("ACC001", result.get(0).getAccountNumber());
    }

    @Test
    void deleteAccount_whenAccountExists_deletesSuccessfully() {
        // Arrange
        when(accountRepository.existsById(1L)).thenReturn(true);

        // Act
        accountService.deleteAccount(1L);

        // Assert
        verify(accountRepository).deleteById(1L);
    }

    @Test
    void deleteAccount_whenAccountNotFound_throwsAccountNotFoundException() {
        // Arrange
        when(accountRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount(999L));
    }
}
