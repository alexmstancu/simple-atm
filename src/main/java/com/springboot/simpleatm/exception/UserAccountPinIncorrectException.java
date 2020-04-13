package com.springboot.simpleatm.exception;

import lombok.Getter;

@Getter
public class UserAccountPinIncorrectException extends UserAccountOperationException {
    public UserAccountPinIncorrectException(String accountNumber) {
        super(accountNumber, "the pin was incorrect for the accountNumber: " + accountNumber);
    }
}
