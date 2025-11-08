package com.billingSystem.user_authentication.controller;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private final String token;
    private final String role;
    private final String userName;
}
