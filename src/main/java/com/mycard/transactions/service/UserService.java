package com.mycard.transactions.service;

import com.mycard.transactions.dto.UserDTO;

import java.util.concurrent.CompletableFuture;

public interface UserService {

    CompletableFuture<UserDTO> getValidUser(Long id);

}
