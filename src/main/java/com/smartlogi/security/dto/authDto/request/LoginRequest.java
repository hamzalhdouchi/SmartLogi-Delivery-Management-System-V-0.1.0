package com.smartlogi.security.dto.authDto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}