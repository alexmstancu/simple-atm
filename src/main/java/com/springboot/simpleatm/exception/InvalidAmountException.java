package com.springboot.simpleatm.exception;

public class InvalidAmountException extends Exception {
    public InvalidAmountException(Double amount) {
        super("amount must not be null, nor less than or equal to 0; amount: " + amount);
    }
}
