package org.northernarc.testingassignment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.northernarc.testingassignment.entity.Account;
import org.northernarc.testingassignment.entity.Customer;
import org.northernarc.testingassignment.entity.Transaction;
import org.northernarc.testingassignment.exception.AccountNotFoundException;
import org.northernarc.testingassignment.exception.InsufficientBalanceException;
import org.northernarc.testingassignment.repository.AccountRepository;
import org.northernarc.testingassignment.repository.TransactionRepository;
import org.northernarc.testingassignment.service.impl.TransactionServiceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Customer testCustomer;
    private Account sourceAccount;
    private Account destinationAccount;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer(1L, "John Doe", "john@example.com", "pass", "9876543210");
        sourceAccount = new Account(1L, "ACC001", testCustomer, Account.AccountType.SAVINGS, 5000.0);
        destinationAccount = new Account(2L, "ACC002", testCustomer, Account.AccountType.CURRENT, 2000.0);
    }

    @Test
    void transfer_whenBothAccountsExistAndBalanceSufficient_createsTransactionRecords() {
        // Arrange
        double transferAmount = 1000.0;
        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(destinationAccount));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        transactionService.transfer(1L, 2L, transferAmount);

        // Assert
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(transactionRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    void transfer_whenSourceAndDestinationAreTheSame_throwsIllegalArgumentException() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.transfer(1L, 1L, 1000.0));
    }

    @Test
    void transfer_whenSourceAccountNotFound_throwsAccountNotFoundException() {
        // Arrange
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> transactionService.transfer(999L, 2L, 1000.0));
    }

    @Test
    void transfer_whenDestinationAccountNotFound_throwsAccountNotFoundException() {
        // Arrange
        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AccountNotFoundException.class, () -> transactionService.transfer(1L, 999L, 1000.0));
    }

    @Test
    void transfer_whenInsufficientBalance_throwsInsufficientBalanceException() {
        // Arrange
        double transferAmount = 6000.0;
        when(accountRepository.findById(1L)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(destinationAccount));

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () -> transactionService.transfer(1L, 2L, transferAmount));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void transfer_whenAmountIsZeroOrNegative_throwsIllegalArgumentException() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.transfer(1L, 2L, -100.0));
        assertThrows(IllegalArgumentException.class, () -> transactionService.transfer(1L, 2L, 0.0));
    }

    @Test
    void getTransactionById_whenTransactionExists_returnsTransaction() {
        // Arrange
        Transaction transaction = new Transaction(1L, sourceAccount, Transaction.TransactionType.TRANSFER_OUT, 1000.0, LocalDateTime.now(), "Test transfer");
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        // Act
        Transaction result = transactionService.getTransactionById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(Transaction.TransactionType.TRANSFER_OUT, result.getTransactionType());
    }

    @Test
    void getTransactionsByAccountId_returnsAccountTransactions() {
        // Arrange
        Transaction transaction = new Transaction(1L, sourceAccount, Transaction.TransactionType.TRANSFER_OUT, 500.0, LocalDateTime.now(), "Transfer");
        when(accountRepository.existsById(1L)).thenReturn(true);
        when(transactionRepository.findByAccountId(1L)).thenReturn(List.of(transaction));

        // Act
        List<Transaction> result = transactionService.getTransactionsByAccountId(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(Transaction.TransactionType.TRANSFER_OUT, result.get(0).getTransactionType());
    }

    @Test
    void getAllTransactions_returnsAllTransactions() {
        // Arrange
        List<Transaction> transactions = List.of(
            new Transaction(1L, sourceAccount, Transaction.TransactionType.DEPOSIT, 500.0, LocalDateTime.now(), "Deposit"),
            new Transaction(2L, sourceAccount, Transaction.TransactionType.WITHDRAWAL, 200.0, LocalDateTime.now(), "Withdrawal")
        );
        when(transactionRepository.findAll()).thenReturn(transactions);

        // Act
        List<Transaction> result = transactionService.getAllTransactions();

        // Assert
        assertEquals(2, result.size());
    }
}
