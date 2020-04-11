package com.springboot.simpleatm.account;

import com.springboot.simpleatm.error.InsufficientBalanceException;
import com.springboot.simpleatm.error.InvalidAmountException;
import com.springboot.simpleatm.error.UserAccountNotFoundException;
import com.springboot.simpleatm.model.UserAccount;

public interface UserAccountService {
    UserAccount deposit(String accountNumber, double amount) throws UserAccountNotFoundException, InvalidAmountException;
    UserAccount withdraw(String accountNumber, double amount) throws UserAccountNotFoundException, InvalidAmountException, InsufficientBalanceException;
    UserAccount fetchAccountDetails(String accountNumber) throws UserAccountNotFoundException;
}
