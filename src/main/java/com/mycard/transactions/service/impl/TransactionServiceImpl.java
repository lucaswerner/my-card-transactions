package com.mycard.transactions.service.impl;

import com.mycard.transactions.client.UsersClient;
import com.mycard.transactions.dto.UserDTO;
import com.mycard.transactions.entity.Transaction;
import com.mycard.transactions.repository.TransactionRepository;
import com.mycard.transactions.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UsersClient usersClient;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UsersClient usersClient) {
        this.transactionRepository = transactionRepository;
        this.usersClient = usersClient;
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
    public Transaction saveTransaction(Transaction transaction) {

        final UserDTO user = usersClient.getUser(transaction.getUserId());

        System.out.println(user);

        return transactionRepository.save(transaction);
    }
}
