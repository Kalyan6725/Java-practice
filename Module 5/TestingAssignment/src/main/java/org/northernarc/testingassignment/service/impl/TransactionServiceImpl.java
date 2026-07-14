package org.northernarc.testingassignment.service.impl;

import org.northernarc.testingassignment.entity.Transaction;
import org.northernarc.testingassignment.service.TransactionService;

import java.util.List;
import java.util.Collections;

public class TransactionServiceImpl implements TransactionService {

    @Override
    public Transaction transfer(Long fromAccountId, Long toAccountId, Double amount) {
        return new Transaction();
    }

    @Override
    public Transaction getTransactionById(Long id) {
        return new Transaction();
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return Collections.emptyList();
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return Collections.emptyList();
    }
}
