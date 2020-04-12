package com.springboot.simpleatm.model.security;

import lombok.Data;

@Data
public class AuthenticationPayload {
    private String pin;
}
