package com.mycard.transactions.service;

import com.mycard.transactions.entity.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface TransactionService {
    Optional<Transaction> getTransaction(Long id);

    List<Transaction> getTransactionList();

    Transaction saveTransaction(Transaction Transaction) throws ExecutionException, InterruptedException;
}
