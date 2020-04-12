package com.springboot.simpleatm.exception;

import lombok.Getter;

@Getter
public class UserAccountNotFoundException extends RuntimeException {
    private final String accountNumber;

    public UserAccountNotFoundException(String accountNumber) {
        super("account number: " + accountNumber);
        this.accountNumber = accountNumber;
    }
}
