package com.mycard.transactions.service.impl;

import com.mycard.transactions.dto.CardDTO;
import com.mycard.transactions.dto.PostTransactionDTO;
import com.mycard.transactions.dto.TransactionDTO;
import com.mycard.transactions.dto.UserDTO;
import com.mycard.transactions.entity.Transaction;
import com.mycard.transactions.repository.TransactionRepository;
import com.mycard.transactions.service.CardService;
import com.mycard.transactions.service.TransactionService;
import com.mycard.transactions.service.UserService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Service
@CacheConfig(cacheNames = "TransactionService")
public class TransactionServiceImpl implements TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private static final int USER_TRANSACTIONS_PER_PAGE = 10;

    private TransactionRepository transactionRepository;
    private UserService userService;
    private CardService cardService;
    private ModelMapper modelMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserService userService, CardService cardService, ModelMapper modelMapper) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.cardService = cardService;
        this.modelMapper = modelMapper;
    }

    public Transaction saveTransaction(Transaction transaction) throws ExecutionException, InterruptedException {

        final CompletableFuture<Optional<UserDTO>> completableFutureUser = userService.getUser(transaction.getUserId());
        final CompletableFuture<Optional<CardDTO>> completableFutureCard =
                cardService.getCard(transaction.getCardBin(), transaction.getCardNumber(), transaction.getUserId());

        CompletableFuture.allOf(
                completableFutureUser,
                completableFutureCard).join();

        final Optional<UserDTO> optionalUser = completableFutureUser.get();

        if (optionalUser.isEmpty())
            throw new IllegalStateException("The indicated user does not exist!");

        final UserDTO user = optionalUser.get();

        if (!user.getEnabled())
            throw new IllegalStateException("The indicated user is not active!");

        final Optional<CardDTO> optionalCard = completableFutureCard.get();

        if (optionalCard.isEmpty())
            throw new IllegalStateException("The indicated card does not exist!");

        final CardDTO card = optionalCard.get();

        if (isCardExpired(card.getExpiration(), LocalDate.now()))
            throw new IllegalStateException("The indicated card expired!");

        LOGGER.info("Saving new transaction: " + transaction);

        return transactionRepository.save(transaction);
    }

    private boolean isCardExpired(LocalDate cardExpiration, LocalDate today) {
        return cardExpiration.getMonthValue() < today.getMonthValue() && cardExpiration.getYear() < today.getYear();
    }

    public Page<Transaction> getTransactionPageByUserId(Long userId, Pageable pageable) {
        return transactionRepository.findAllByUserId(userId, pageable);
    }

    public Optional<Transaction> getTransactionByIdAndUserId(Long id, Long userId) {
        return transactionRepository.findByIdAndUserId(id, userId);
    }

    @HystrixCommand(threadPoolKey = "saveTransactionDTOThreadPool")
    public TransactionDTO saveTransaction(PostTransactionDTO postTransactionDTO) throws ExecutionException, InterruptedException {
        return transformTransactionToTransactionDTO(
                saveTransaction(modelMapper.map(postTransactionDTO, Transaction.class)));
    }

    @HystrixCommand(threadPoolKey = "getTransactionDTOPageByUserIdThreadPool")
    public Page<TransactionDTO> getTransactionDTOPageByUserId(Long userId, Integer pageNumber) {
        return getTransactionPageByUserId(
                userId,
                PageRequest.of(pageNumber, USER_TRANSACTIONS_PER_PAGE, Sort.by("timestamp").descending())
        )
                .map(this::transformTransactionToTransactionDTO);
    }

    @Cacheable(key = "{#userId, #id}")
    @HystrixCommand(threadPoolKey = "getTransactionDTOByIdAndUserIdThreadPool")
    public Optional<TransactionDTO> getTransactionDTOByIdAndUserId(Long id, Long userId) {
        return getTransactionByIdAndUserId(id, userId)
                .map(this::transformTransactionToTransactionDTO);
    }

    private TransactionDTO transformTransactionToTransactionDTO(Transaction transaction) {
        return modelMapper.map(transaction, TransactionDTO.class);
    }
}
