package com.springboot.simpleatm.exception;

import lombok.Getter;

@Getter
public class UserAccountNotFoundException extends UserAccountOperationException {
    public UserAccountNotFoundException(String accountNumber) {
        super(accountNumber, "account number not found: " + accountNumber);
    }
}
