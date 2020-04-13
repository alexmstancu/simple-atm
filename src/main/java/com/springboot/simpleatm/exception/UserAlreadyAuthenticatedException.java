package com.springboot.simpleatm.exception;

import lombok.Getter;

@Getter
public class UserAlreadyAuthenticatedException extends UserAccountOperationException {
    public UserAlreadyAuthenticatedException(String accountNumber) {
        super(accountNumber, "user with account number is already authenticated: " + accountNumber);
    }
}
