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
    public CompletableFuture<UserDTO> getValidUser(Long id) {
        final Optional<UserDTO> optionalUser = userClient.getUser(id);

        if (optionalUser.isEmpty())
            throw new IllegalStateException("The indicated user does not exist!");

        final UserDTO user = optionalUser.get();

        if (!user.getEnabled())
            throw new IllegalStateException("The indicated user is not active!");

        return CompletableFuture.completedFuture(user);
    }
}
