package com.springboot.simpleatm.exception;

public class UserAccountNotFoundException extends Exception {
    public UserAccountNotFoundException(String accountNumber) {
        super("account number: " + accountNumber);
    }
}
