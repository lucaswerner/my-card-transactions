package com.mycard.transactions.service;

import com.mycard.transactions.dto.PostTransactionDTO;
import com.mycard.transactions.dto.TransactionDTO;
import com.mycard.transactions.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface TransactionService {
    Transaction saveTransaction(Transaction transaction) throws ExecutionException, InterruptedException;

    Page<Transaction> getTransactionPageByUserId(Long userId, Pageable pageable);

    Optional<Transaction> getTransactionByIdAndUserId(Long id, Long userId);

    TransactionDTO saveTransaction(PostTransactionDTO postTransactionDTO) throws ExecutionException, InterruptedException;

    Page<TransactionDTO> getTransactionDTOPageByUserId(Long userId, Integer pageNumber);

    Optional<TransactionDTO> getTransactionDTOByIdAndUserId(Long id, Long userId);
}
