package com.mycard.transactions.service;

import com.mycard.transactions.dto.CardDTO;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface CardService {

    CompletableFuture<CardDTO> getValidCard(Long bin, Long number, Long userId);

}
