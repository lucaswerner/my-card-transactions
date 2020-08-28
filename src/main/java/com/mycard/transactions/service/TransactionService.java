package com.mycard.transactions.service;

import com.mycard.transactions.dto.PostTransactionDTO;
import com.mycard.transactions.dto.TransactionDTO;
import com.mycard.transactions.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionService {
    Transaction saveTransaction(Transaction transaction);

    Page<Transaction> getTransactionPageByUserId(Long userId, Pageable pageable);

    Optional<Transaction> getTransactionByIdAndUserId(Long id, Long userId);

    List<Transaction> getTransactionListByCardAndDate(
            Long cardBin,
            Long cardNumber,
            LocalDate timeStampStart,
            LocalDate timestampEnd
    );

    TransactionDTO saveTransaction(PostTransactionDTO postTransactionDTO);

    Page<TransactionDTO> getTransactionDTOPageByUserId(Long userId, Integer pageNumber);

    Optional<TransactionDTO> getTransactionDTOByIdAndUserId(Long id, Long userId);

    List<TransactionDTO> getTransactionDTOListByCardAndDate(
            Long cardBin,
            Long cardNumber,
            LocalDate timeStampStart,
            LocalDate timestampEnd
    );
}
