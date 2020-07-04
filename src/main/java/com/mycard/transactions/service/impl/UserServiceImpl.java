package com.mycard.transactions.service.impl;

import com.mycard.transactions.client.UserClient;
import com.mycard.transactions.dto.UserDTO;
import com.mycard.transactions.service.UserService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService {

    private UserClient userClient;

    public UserServiceImpl(UserClient userClient) {
        this.userClient = userClient;
    }

    @Async
    @Override
    public CompletableFuture<Optional<UserDTO>> getUser(Long id) {
        return CompletableFuture.completedFuture(userClient.getUser(id));
    }
}
