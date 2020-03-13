package com.mycard.transactions.dto;

import lombok.Data;

import java.time.LocalDateTime;

public @Data
class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean stActive = Boolean.TRUE;
    private LocalDateTime timestamp;
}
