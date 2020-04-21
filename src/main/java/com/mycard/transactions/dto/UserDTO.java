package com.mycard.transactions.dto;

import lombok.Data;

import java.time.LocalDate;

public @Data
class UserDTO {
    private Long id;
    private String name;
    private String email;
    private boolean enabled;
    private LocalDate lastLogin;
}
