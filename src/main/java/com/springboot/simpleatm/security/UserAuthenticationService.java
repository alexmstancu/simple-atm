package com.springboot.simpleatm.security;

public interface UserAuthenticationService {
    void authenticate(String accountNumber, String pin);
    void verifyUserIsAuthenticated(String accountNumber);
    void deauthenticate(String accountNumber);
}
