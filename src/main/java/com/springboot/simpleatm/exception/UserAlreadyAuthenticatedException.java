package com.springboot.simpleatm.exception;

import lombok.Getter;

@Getter
public class UserAlreadyAuthenticatedException extends RuntimeException {
    private final String accountNumber;

    public UserAlreadyAuthenticatedException(String accountNumber) {
        super("user with account number is already authenticated " + accountNumber);
        this.accountNumber = accountNumber;
    }
}
