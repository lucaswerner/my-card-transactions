package com.mycard.transactions.dto;

import com.mycard.transactions.enumeration.TransactionType;
import lombok.Data;

public @Data
class PostTransactionDTO {
    private TransactionType transactionType;
    private Long userId;
    private Long cardBin;
    private Long cardNumber;
    private double value;
}
