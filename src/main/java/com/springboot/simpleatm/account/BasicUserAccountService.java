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
    public void deposit(String accountNumber, double amount) {
        UserAccount userAccount = findAccountByNumber(accountNumber);
        validateAmount(amount);
        double newBalance = userAccount.getBalance() + amount;
        updateBalance(userAccount, newBalance);
    }

    @Override
    public synchronized double withdraw(String accountNumber, double amount) {
        UserAccount userAccount = findAccountByNumber(accountNumber);
        validateAmount(amount);
        double currentBalance = userAccount.getBalance();
        if (currentBalance < amount) {
            throw new InsufficientBalanceException("current balance: " + currentBalance + ", amount to be withdrawn: " + amount);
        }
        double newBalance = currentBalance - amount;
        updateBalance(userAccount, newBalance);
        return amount;
    }

    @Override
    public double viewBalance(String accountNumber) {
        UserAccount userAccount = findAccountByNumber(accountNumber);
        return userAccount.getBalance();
    }

    private UserAccount findAccountByNumber(String accountNumber) {
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

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("amount: " + amount);
        }
    }
}
