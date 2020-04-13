package com.springboot.simpleatm.exception;

import lombok.Getter;

@Getter
public class InvalidAmountException extends UserAccountOperationException {
    public InvalidAmountException(String accountNumber, Double amount) {
        super(accountNumber, "invalid amount, amount must not be null, nor less than or equal to 0; amount: " + amount);
    }
}
