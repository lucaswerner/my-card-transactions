package com.mycard.transactions.service;

import com.mycard.transactions.dto.CardDTO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface CardService {

    CompletableFuture<Optional<CardDTO>> getCard(@PathVariable("bin") Long bin, @PathVariable("number") Long number);

}
