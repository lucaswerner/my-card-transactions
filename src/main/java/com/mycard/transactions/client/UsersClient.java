package com.mycard.transactions.client;

import com.mycard.transactions.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("users")
public interface UsersClient {

    @GetMapping("/api/v1/users/{id}")
    UserDTO getUser(@PathVariable Long id);
}
