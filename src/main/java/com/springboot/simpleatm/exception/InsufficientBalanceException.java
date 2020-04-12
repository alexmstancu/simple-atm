package com.springboot.simpleatm.exception;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(Double currentBalance, Double amount) {
        super("current balance: " + currentBalance + ", requested withdraw amount: " + amount);
    }
}
