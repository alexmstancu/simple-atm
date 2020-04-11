package com.springboot.simpleatm.error;

public class UserAccountNotFoundException extends Exception {
    public UserAccountNotFoundException(String message) {
        super(message);
    }
}
