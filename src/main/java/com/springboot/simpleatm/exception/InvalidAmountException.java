package com.springboot.simpleatm.exception;

import lombok.Getter;

@Getter
public class InvalidAmountException extends RuntimeException {
    private final Double amount;

    public InvalidAmountException(Double amount) {
        super("amount must not be null, nor less than or equal to 0; amount: " + amount);
        this.amount = amount;
    }
}
