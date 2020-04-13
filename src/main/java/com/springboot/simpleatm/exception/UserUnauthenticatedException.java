package com.springboot.simpleatm.exception;

import lombok.Getter;

@Getter
public class UserUnauthenticatedException extends UserAccountOperationException {
    public UserUnauthenticatedException(String accountNumber) {
        super(accountNumber, "user for the following account is not authenticated: " + accountNumber);
    }
}
