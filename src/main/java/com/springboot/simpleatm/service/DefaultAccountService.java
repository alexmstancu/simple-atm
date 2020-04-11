package com.springboot.simpleatm.service;

import com.springboot.simpleatm.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultAccountService implements AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public DefaultAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void deposit(String accountNumber, double amount) {

    }

    @Override
    public double withdraw(String accountNumber, double amount) {
        return 0;
    }

    @Override
    public double viewBalance(String accountNumber) {
        return 0;
    }
}
