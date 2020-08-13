package com.mycard.transactions.service.impl;

import com.mycard.transactions.client.CardClient;
import com.mycard.transactions.dto.CardDTO;
import com.mycard.transactions.service.CardService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class CardServiceImpl implements CardService {

    private CardClient cardClient;

    public CardServiceImpl(CardClient cardClient) {
        this.cardClient = cardClient;
    }

    @Async
    public CompletableFuture<CardDTO> getValidCard(Long bin, Long number, Long userId) {
        final Optional<CardDTO> optionalCard = cardClient.getCard(bin, number, userId);

        if (optionalCard.isEmpty())
            throw new IllegalStateException("The indicated card does not exist!");

        final CardDTO card = optionalCard.get();

        if (isCardExpired(card.getExpiration(), LocalDate.now()))
            throw new IllegalStateException("The indicated card expired!");

        return null;
    }

    private boolean isCardExpired(LocalDate cardExpiration, LocalDate today) {
        return cardExpiration.getMonthValue() < today.getMonthValue() && cardExpiration.getYear() < today.getYear();
    }
}
