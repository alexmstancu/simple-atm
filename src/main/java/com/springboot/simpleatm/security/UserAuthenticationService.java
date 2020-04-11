package com.springboot.simpleatm.security;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public interface UserAuthenticationService {
    public String authenticate(String accountNumber, String pin);
    public boolean isAuthenticated(String accountNumber, String token);
    public void deauthenticate(String accountNumber);
}
