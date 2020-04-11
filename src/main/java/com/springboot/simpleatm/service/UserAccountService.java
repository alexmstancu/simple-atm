package com.springboot.simpleatm.service;

import org.springframework.stereotype.Service;

public interface UserAccountService {
    public void deposit(String accountNumber, double amount);
    public double withdraw(String accountNumber, double amount);
    public double viewBalance(String accountNumber);
}
