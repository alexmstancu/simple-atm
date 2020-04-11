package com.springboot.simpleatm.account;

import com.springboot.simpleatm.exception.InsufficientBalanceException;
import com.springboot.simpleatm.exception.InvalidAmountException;
import com.springboot.simpleatm.exception.UserAccountNotFoundException;
import com.springboot.simpleatm.model.UserAccount;

public interface UserAccountService {
    UserAccount deposit(String accountNumber, Double amount) throws UserAccountNotFoundException, InvalidAmountException;
    UserAccount withdraw(String accountNumber, Double amount) throws UserAccountNotFoundException, InvalidAmountException, InsufficientBalanceException;
    UserAccount fetchAccountDetails(String accountNumber) throws UserAccountNotFoundException;
}
