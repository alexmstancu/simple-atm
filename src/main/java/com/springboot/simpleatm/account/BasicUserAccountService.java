package com.springboot.simpleatm.account;

import com.springboot.simpleatm.exception.InsufficientBalanceException;
import com.springboot.simpleatm.exception.InvalidAmountException;
import com.springboot.simpleatm.exception.UserAccountNotFoundException;
import com.springboot.simpleatm.model.UserAccount;
import com.springboot.simpleatm.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BasicUserAccountService implements UserAccountService {
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public BasicUserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserAccount fetchAccountDetails(String accountNumber) throws UserAccountNotFoundException {
        return findAccountByNumber(accountNumber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserAccount deposit(String accountNumber, Double amount) throws UserAccountNotFoundException, InvalidAmountException {
        validateAmount(amount);
        UserAccount userAccount = findAccountByNumber(accountNumber);
        Double newBalance = userAccount.getBalance() + amount;
        updateBalance(userAccount, newBalance);
        return userAccount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserAccount withdraw(String accountNumber, Double amount) throws UserAccountNotFoundException, InvalidAmountException, InsufficientBalanceException {
        validateAmount(amount);
        UserAccount userAccount = findAccountByNumber(accountNumber);
        Double currentBalance = userAccount.getBalance();
        if (currentBalance < amount) {
            throw new InsufficientBalanceException(currentBalance, amount);
        }
        Double newBalance = currentBalance - amount;
        updateBalance(userAccount, newBalance);
        return userAccount;
    }

    private UserAccount findAccountByNumber(String accountNumber) throws UserAccountNotFoundException {
        // premise: the account numbers are unique
        Optional<UserAccount> optional = Optional.ofNullable(userAccountRepository.findByAccountNumber(accountNumber));
        return optional.orElseThrow(() -> new UserAccountNotFoundException(accountNumber));
    }

    private void updateBalance(UserAccount userAccount, Double newBalance) {
        userAccount.setBalance(newBalance);
        userAccountRepository.save(userAccount);
    }

    private void validateAmount(Double amount) throws InvalidAmountException {
        if (amount == null || amount <= 0) {
            throw new InvalidAmountException(amount);
        }
    }
}
