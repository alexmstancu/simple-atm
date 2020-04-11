package com.springboot.simpleatm.account;

import com.springboot.simpleatm.error.InsufficientBalanceException;
import com.springboot.simpleatm.error.InvalidAmountException;
import com.springboot.simpleatm.error.UserAccountNotFoundException;
import com.springboot.simpleatm.model.UserAccount;
import com.springboot.simpleatm.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.springboot.simpleatm.repository.UserAccountRepository.filterByAccountNumber;

@Service
public class BasicUserAccountService implements UserAccountService {
    private UserAccountRepository userAccountRepository;

    @Autowired
    public BasicUserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserAccount deposit(String accountNumber, double amount) throws UserAccountNotFoundException, InvalidAmountException {
        UserAccount userAccount = findAccountByNumber(accountNumber);
        validateAmount(amount);
        double newBalance = userAccount.getBalance() + amount;
        updateBalance(userAccount, newBalance);
        return userAccount;
    }

    @Override
    public UserAccount withdraw(String accountNumber, double amount) throws UserAccountNotFoundException, InvalidAmountException, InsufficientBalanceException {
        UserAccount userAccount = findAccountByNumber(accountNumber);
        validateAmount(amount);
        double currentBalance = userAccount.getBalance();
        if (currentBalance < amount) {
            throw new InsufficientBalanceException("current balance: " + currentBalance + ", amount to be withdrawn: " + amount);
        }
        double newBalance = currentBalance - amount;
        updateBalance(userAccount, newBalance);
        return userAccount;
    }

    @Override
    public UserAccount fetchAccountDetails(String accountNumber) throws UserAccountNotFoundException {
        return findAccountByNumber(accountNumber);
    }

    private UserAccount findAccountByNumber(String accountNumber) throws UserAccountNotFoundException {
        // premise: the account numbers are unique
        Optional<UserAccount> optional = userAccountRepository.findOne(filterByAccountNumber(accountNumber));
        if (!optional.isPresent()) {
            throw new UserAccountNotFoundException("account number: " + accountNumber);
        }
        return optional.get();
    }

    private void updateBalance(UserAccount userAccount, double newBalance) {
        userAccount.setBalance(newBalance);
        userAccountRepository.save(userAccount);
    }

    private void validateAmount(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("amount: " + amount);
        }
    }
}
