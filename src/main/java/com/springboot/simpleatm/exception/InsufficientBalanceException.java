package com.springboot.simpleatm.exception;

import lombok.Getter;

@Getter
public class InsufficientBalanceException extends UserAccountOperationException {
    public InsufficientBalanceException(String accountNumber, Double currentBalance, Double amount) {
        super(accountNumber, "insufficient balance; current balance: " + currentBalance + ", requested withdraw amount: " + amount);
    }
}
