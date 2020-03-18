package com.mycard.transactions.client;

import com.mycard.transactions.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(value = "my-card-users", path = "/api/v1/users", qualifier = "user-client")
public interface UserClient {

    @GetMapping("/{id}")
    Optional<UserDTO> getUser(@PathVariable("id") Long id);
}
