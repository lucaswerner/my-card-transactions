package com.mycard.transactions.service;

import com.mycard.transactions.dto.UserDTO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserService {

    CompletableFuture<Optional<UserDTO>> getUser(Long id);

}
