package com.springboot.simpleatm.security;

public interface UserAuthenticationService {
    String authenticate(String accountNumber, String pin);
    void verifyUserIsAuthenticated(String accountNumber, String token);
    void deauthenticate(String accountNumber);
}
