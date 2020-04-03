package com.mycard.transactions.service.impl;

import com.mycard.transactions.client.UserClient;
import com.mycard.transactions.dto.UserDTO;
import com.mycard.transactions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserClient userClient;

    @Async
    @Override
    public CompletableFuture<Optional<UserDTO>> getUser(Long id) {
        return CompletableFuture.completedFuture(userClient.getUser(id));
    }
}
