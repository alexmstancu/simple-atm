package com.springboot.simpleatm.account;

import com.springboot.simpleatm.error.InvalidAmountException;
import com.springboot.simpleatm.error.UserAccountNotFoundException;

public interface UserAccountService {
    public void deposit(String accountNumber, double amount) throws UserAccountNotFoundException, InvalidAmountException;
    public double withdraw(String accountNumber, double amount) throws UserAccountNotFoundException, InvalidAmountException;
    public double viewBalance(String accountNumber) throws UserAccountNotFoundException;
}
