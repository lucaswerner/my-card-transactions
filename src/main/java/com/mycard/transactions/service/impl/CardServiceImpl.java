package com.mycard.transactions.service.impl;

import com.mycard.transactions.client.CardClient;
import com.mycard.transactions.dto.CardDTO;
import com.mycard.transactions.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardClient cardClient;

    @Async
    @Override
    public CompletableFuture<Optional<CardDTO>> getCard(Long bin, Long number) {
        return CompletableFuture.completedFuture(cardClient.getCard(bin, number));
    }
}
