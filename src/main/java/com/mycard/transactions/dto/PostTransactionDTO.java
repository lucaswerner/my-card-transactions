package com.mycard.transactions.dto;

import com.mycard.transactions.enumeration.TransactionType;
import lombok.Data;

import javax.validation.constraints.NotNull;

public @Data
class PostTransactionDTO {
    @NotNull
    private TransactionType transactionType;
    @NotNull
    private Long userId;
    @NotNull
    private Long cardBin;
    @NotNull
    private Long cardNumber;
    @NotNull
    private double value;
}
