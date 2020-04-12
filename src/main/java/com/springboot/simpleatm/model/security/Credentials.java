package com.springboot.simpleatm.model.security;

import lombok.Data;

@Data
public class Credentials {
    private String accountNumber;
    private String pin;
}
