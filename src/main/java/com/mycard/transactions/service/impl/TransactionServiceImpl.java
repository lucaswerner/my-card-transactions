package com.mycard.transactions.service.impl;

import com.mycard.transactions.client.CardClient;
import com.mycard.transactions.client.UserClient;
import com.mycard.transactions.dto.CardDTO;
import com.mycard.transactions.dto.UserDTO;
import com.mycard.transactions.entity.Transaction;
import com.mycard.transactions.repository.TransactionRepository;
import com.mycard.transactions.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private CardClient cardClient;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserClient userClient, CardClient cardClient) {
        this.transactionRepository = transactionRepository;
        this.userClient = userClient;
        this.cardClient = cardClient;
    }

    @Override
    public Optional<Transaction> getTransaction(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public List<Transaction> getTransactionList() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) throws ExecutionException, InterruptedException {

        final CompletableFuture<Optional<UserDTO>> completableFutureUser =
                CompletableFuture.supplyAsync(() -> userClient.getUser(transaction.getUserId()));

        final CompletableFuture<Optional<CardDTO>> completableFutureCard =
                CompletableFuture.supplyAsync(() -> cardClient.getCard(transaction.getCardBin(), transaction.getCardNumber()));

        final CompletableFuture<Void> combinedFutureUserAndCard =
                CompletableFuture.allOf(completableFutureUser, completableFutureCard);

        combinedFutureUserAndCard.get();

        final Optional<UserDTO> optionalUser = completableFutureUser.get();

        if (optionalUser.isEmpty())
            throw new IllegalStateException("The indicated user does not exist!");

        final UserDTO user = optionalUser.get();

        if (!user.getStActive())
            throw new IllegalStateException("The indicated user is not active!");

        final Optional<CardDTO> optionalCard = completableFutureCard.get();

        if (optionalCard.isEmpty())
            throw new IllegalStateException("The indicated card does not exist!");

        final CardDTO card = optionalCard.get();
        final LocalDate cardExpiration = card.getExpiration();
        final LocalDate today = LocalDate.now();
        if (cardExpiration.getMonthValue() < today.getMonthValue()
                || cardExpiration.getYear() < today.getYear())
            throw new IllegalStateException("The indicated card expired!");

        LOGGER.info("Saving new transaction!");

        return transactionRepository.save(transaction);
    }
}
