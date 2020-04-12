package com.springboot.simpleatm.security;

import com.springboot.simpleatm.exception.UserAccountNotFoundException;
import com.springboot.simpleatm.exception.UserAccountPinIncorrectException;
import com.springboot.simpleatm.exception.UserAlreadyAuthenticatedException;
import com.springboot.simpleatm.exception.UserUnauthenticatedException;
import com.springboot.simpleatm.model.UserAccount;
import com.springboot.simpleatm.model.security.Token;
import com.springboot.simpleatm.repository.UserAccountRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class BasicUserAuthenticationService implements UserAuthenticationService {
    private static final Duration DEFAULT_TOKEN_DURATION = Duration.ofMinutes(1);
    private static final int TOKEN_LENGTH = 10;
    private final ConcurrentMap<String, Token> authenticatedUserTokens = new ConcurrentHashMap<>();
    private final UserAccountRepository repository;

    @Autowired
    public BasicUserAuthenticationService(UserAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public String authenticate(String accountNumber, String pin) {
        if (authenticatedUserTokens.containsKey(accountNumber)) {
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
        Token newToken = generateAccessToken(accountNumber);
        authenticatedUserTokens.put(accountNumber, newToken);
        return newToken.getValue();
    }

    @Override
    public void verifyUserIsAuthenticated(String accountNumber, String userTokenValue) {
        Token existingToken = authenticatedUserTokens.get(accountNumber);
        if (existingToken == null || !existingToken.getValue().equals(userTokenValue) || isTokenExpired(existingToken)) {
            throw new UserUnauthenticatedException(accountNumber);
        }
    }

    @Override
    public void deauthenticate(String accountNumber) {
        authenticatedUserTokens.remove(accountNumber);
    }

    private Token generateAccessToken(String accountNumber) {
        String tokenValue = RandomStringUtils.randomAscii(TOKEN_LENGTH) + "-" + accountNumber;
        return Token.builder()
                .value(tokenValue)
                .duration(DEFAULT_TOKEN_DURATION)
                .creationTime(Instant.now())
                .build();
    }

    private boolean isTokenExpired(Token token) {
        Instant tokenExpirationTime = token.getCreationTime().plus(token.getDuration());
        return Instant.now().isAfter(tokenExpirationTime);
    }
}
