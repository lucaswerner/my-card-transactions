package com.mycard.transactions.service.impl;

import com.mycard.transactions.dto.CardDTO;
import com.mycard.transactions.dto.UserDTO;
import com.mycard.transactions.entity.Transaction;
import com.mycard.transactions.repository.TransactionRepository;
import com.mycard.transactions.service.CardService;
import com.mycard.transactions.service.TransactionService;
import com.mycard.transactions.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    private UserService userService;

    @Autowired
    private CardService cardService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserService userService, CardService cardService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.cardService = cardService;
    }

    @Override
    @HystrixCommand(threadPoolKey = "getTransactionThreadPool")
    public Optional<Transaction> getTransaction(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    @HystrixCommand(threadPoolKey = "getTransactionListThreadPool")
    public List<Transaction> getTransactionList() {
        return transactionRepository.findAll();
    }

    @Override
    @HystrixCommand(threadPoolKey = "saveTransactionThreadPool")
    public Transaction saveTransaction(Transaction transaction) throws ExecutionException, InterruptedException {

        final CompletableFuture<Optional<UserDTO>> completableFutureUser = userService.getUser(transaction.getUserId());
        final CompletableFuture<Optional<CardDTO>> completableFutureCard =
                cardService.getCard(transaction.getCardBin(), transaction.getCardNumber());

        CompletableFuture.allOf(completableFutureUser, completableFutureCard).join();

        final Optional<UserDTO> optionalUser = completableFutureUser.get();

        if (optionalUser.isEmpty())
            throw new IllegalStateException("The indicated user does not exist!");

        final UserDTO user = optionalUser.get();

        if (!user.isEnabled())
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

        LOGGER.info("Saving new transaction: " + transaction);

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionListByUserId(Long userId, Pageable pageable) {
        return transactionRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Optional<Transaction> getTransactionByIdAndUserId(Long id, Long userId) {
        return transactionRepository.findByIdAndUserId(id, userId);
    }
}
