package com.springboot.simpleatm.service;

import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    public void deposit(String accountNumber, double amount);
    public double withdraw(String accountNumber, double amount);
    public double viewBalance(String accountNumber);
}
