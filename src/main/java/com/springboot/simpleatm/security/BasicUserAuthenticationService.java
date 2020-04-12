package com.springboot.simpleatm.security;

import com.springboot.simpleatm.exception.UserAccountNotFoundException;
import com.springboot.simpleatm.exception.UserAccountPinIncorrectException;
import com.springboot.simpleatm.exception.UserAlreadyAuthenticatedException;
import com.springboot.simpleatm.exception.UserUnauthenticatedException;
import com.springboot.simpleatm.model.UserAccount;
import com.springboot.simpleatm.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class BasicUserAuthenticationService implements UserAuthenticationService {
    private final ConcurrentMap<String, Boolean> authenticatedUsers = new ConcurrentHashMap<>();
    private final UserAccountRepository repository;

    @Autowired
    public BasicUserAuthenticationService(UserAccountRepository repository) {
        this.repository = repository;
    }

    // this should probably return a token to be used for every request from now on
    @Override
    public void authenticate(String accountNumber, String pin) {
        if (authenticatedUsers.containsKey(accountNumber)) {
            throw new UserAlreadyAuthenticatedException(accountNumber);
        }
        // normally, this call should look in a different repository (probably a different table) where users are stored
        UserAccount userAccount = repository.findByAccountNumber(accountNumber);
        if (userAccount == null) {
            throw new UserAccountNotFoundException(accountNumber);
        }
        if (!userAccount.getPin().equals(pin)) {
            throw new UserAccountPinIncorrectException(accountNumber);
        }
        authenticatedUsers.put(accountNumber, true);
    }

    @Override
    public void verifyUserIsAuthenticated(String accountNumber) {
        Boolean result = authenticatedUsers.get(accountNumber);
        if (result == null) {
            throw new UserUnauthenticatedException(accountNumber);
        }
    }

    @Override
    public void deauthenticate(String accountNumber) {
        authenticatedUsers.remove(accountNumber);
    }
}
