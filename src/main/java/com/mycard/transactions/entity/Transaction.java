package com.mycard.transactions.entity;

import com.mycard.transactions.enumeration.TransactionType;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public @Data
class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long cardBin;

    @Column(nullable = false)
    private Long cardNumber;

    @Column(nullable = false)
    private double value;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

}
