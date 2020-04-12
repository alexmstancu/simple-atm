package com.springboot.simpleatm.exception;

import lombok.Getter;

@Getter
public class UserAccountPinIncorrectException extends RuntimeException {
    private final String accountNumber;

    public UserAccountPinIncorrectException(String accountNumber) {
        super("the pin was incorrect for the accountNumber " + accountNumber);
        this.accountNumber = accountNumber;
    }
}
