package org.northernarc.testingassignment.service;

import org.northernarc.testingassignment.entity.Transaction;
import java.util.List;

public interface TransactionService {
    Transaction transfer(Long fromAccountId, Long toAccountId, Double amount);
    Transaction getTransactionById(Long id);
    List<Transaction> getTransactionsByAccountId(Long accountId);
    List<Transaction> getAllTransactions();
}
