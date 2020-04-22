package com.mycard.transactions.client;

import com.mycard.transactions.dto.CardDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(value = "my-card-cards", path = "/api/v1/cards", qualifier = "card-client")
public interface CardClient {

    @GetMapping("/{bin}/{number}/{userId}")
    Optional<CardDTO> getCard(
            @PathVariable("bin") Long bin,
            @PathVariable("number") Long number,
            @PathVariable("userId") Long userId);
}
