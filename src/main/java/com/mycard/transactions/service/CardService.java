package com.mycard.transactions.service;

import com.mycard.transactions.dto.CardDTO;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface CardService {

    CompletableFuture<Optional<CardDTO>> getCard(Long bin, Long number, Long userId);

}
