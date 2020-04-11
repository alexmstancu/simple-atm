package com.springboot.simpleatm.account;

import com.springboot.simpleatm.error.InsufficientBalanceException;
import com.springboot.simpleatm.error.InvalidAmountException;
import com.springboot.simpleatm.error.UserAccountNotFoundException;
import com.springboot.simpleatm.model.UserAccount;

public interface UserAccountService {
    public UserAccount deposit(String accountNumber, double amount) throws UserAccountNotFoundException, InvalidAmountException;
    public UserAccount withdraw(String accountNumber, double amount) throws UserAccountNotFoundException, InvalidAmountException, InsufficientBalanceException;
    public UserAccount getAccountDetails(String accountNumber) throws UserAccountNotFoundException;
}
