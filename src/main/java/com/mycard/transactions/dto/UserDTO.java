package com.mycard.transactions.dto;

import lombok.Data;

import java.time.LocalDate;

public @Data
class UserDTO {
    private Long id;
    private String email;
    private Boolean enabled;
    private LocalDate lastLogin;
}
