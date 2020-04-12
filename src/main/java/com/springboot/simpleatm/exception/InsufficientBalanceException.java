package com.springboot.simpleatm.exception;

import lombok.Getter;

@Getter
public class InsufficientBalanceException extends RuntimeException {
    private final Double currentBalance;
    private final Double amount;

    public InsufficientBalanceException(Double currentBalance, Double amount) {
        super("current balance: " + currentBalance + ", requested withdraw amount: " + amount);
        this.currentBalance = currentBalance;
        this.amount = amount;
    }
}
