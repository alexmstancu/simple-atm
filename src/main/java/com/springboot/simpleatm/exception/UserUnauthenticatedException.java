package com.springboot.simpleatm.exception;

import lombok.Getter;

@Getter
public class UserUnauthenticatedException extends RuntimeException {
    private final String accountNumber;

    public UserUnauthenticatedException(String accountNumber) {
        super("user for the following account is not authenticated: " + accountNumber);
        this.accountNumber = accountNumber;
    }
}
