package com.springboot.simpleatm.security;

public interface UserAuthenticationService {
    /**
     * Authenticates the given accountNumber's user into the ATM system by verifying that his credentials are correct.
     * This should probably return a token to be used for every request from now on, but for simplicity,
     * we just keep a note in memory that this user is authenticated for as long as the
     * {@link UserAuthenticationService#deauthenticate(String)} is not called.
     */
    void authenticate(String accountNumber, String pin);
    /**
     * Verifies if this this accountNumber's user is authenticated into the ATM system.
     */
    void verifyUserIsAuthenticated(String accountNumber);
    /**
     * Removed the authentication for given accountNumber's user from the ATM system. From now on,
     * for any request, the user will not be allowed any more into the ATM system.
     */
    void deauthenticate(String accountNumber);
}
