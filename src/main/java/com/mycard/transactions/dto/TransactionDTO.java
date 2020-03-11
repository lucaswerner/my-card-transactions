package com.mycard.transactions.dto;

import com.mycard.transactions.enumeration.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

public @Data
class TransactionDTO {
    private Long id;
    private TransactionType transactionType;
    private Long userId;
    private Long cardBin;
    private Long cardNumber;
    private double value;
    private LocalDateTime timestamp = LocalDateTime.now();
}
