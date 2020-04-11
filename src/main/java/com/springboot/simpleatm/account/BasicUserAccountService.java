package com.springboot.simpleatm.account;

import com.springboot.simpleatm.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasicUserAccountService implements UserAccountService {

    private UserAccountRepository userAccountRepository;

    @Autowired
    public BasicUserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
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
