package com.mycard.transactions.service.impl;

import com.mycard.transactions.client.CardClient;
import com.mycard.transactions.dto.CardDTO;
import com.mycard.transactions.service.CardService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CardServiceImpl implements CardService {

    private CardClient cardClient;

    public CardServiceImpl(CardClient cardClient) {
        this.cardClient = cardClient;
    }

    @Async
    @Override
    public CompletableFuture<Optional<CardDTO>> getCard(Long bin, Long number, Long userId) {
        return CompletableFuture.completedFuture(cardClient.getCard(bin, number, userId));
    }
}
