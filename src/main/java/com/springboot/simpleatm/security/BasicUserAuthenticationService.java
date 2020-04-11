package com.springboot.simpleatm.security;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class BasicUserAuthenticationService implements UserAuthenticationService {
    private ConcurrentMap<String, String> authenticatedAccountTokens = new ConcurrentHashMap<>();

    @Override
    public String authenticate(String accountNumber, String pin) {
        return null;
    }

    @Override
    public boolean isAuthenticated(String accountNumber, String token) {
        return false;
    }

    @Override
    public void deauthenticate(String accountNumber) {

    }
}
