package com.springboot.simpleatm.exception;

public class UserAccountNotFoundException extends Exception {
    public UserAccountNotFoundException(String message) {
        super(message);
    }
}
