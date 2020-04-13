package com.springboot.simpleatm.controller;

import com.springboot.simpleatm.account.UserAccountService;
import com.springboot.simpleatm.model.UserAccount;
import com.springboot.simpleatm.model.operation.OperationResult;
import com.springboot.simpleatm.model.operation.OperationType;
import com.springboot.simpleatm.security.UserAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Basic ATM API with very basic authentication. There are 4 operations supported:
 * 1. authentication (with credentials being accountNumber & pin)
 * 2. see the account's details (account holder name, balance, account number)
 * 3. withdraw an amount from an account
 * 4. deposit an amount into an account
 * Operations 2, 3, 4 are not allowed to be carried out without first authenticating into the system.
 * Once a user is authenticated, he is allowed to carry out a single operations out of the 3 aforementioned, after which
 * he will be 'deauthenticated' automatically (most ATMs function like that, or at least they did some time ago).
 * While a user is authenticated into the ATM with his accountNumber & pin, another user trying to authenticate
 * with the same credentials will not be permitted. A single user of an account is allowed to be logged into the system.
 *
 * See {@link RestControllerExceptionHandler} for exception handling.
 */
@CrossOrigin
@RestController
@RequestMapping("/atm/userAccount")
public class ATMController {
    private static final Logger logger = LoggerFactory.getLogger(ATMController.class);

    private final UserAccountService userAccountService;
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public ATMController(UserAccountService userAccountService, UserAuthenticationService userAuthenticationService) {
        this.userAccountService = userAccountService;
        this.userAuthenticationService = userAuthenticationService;
    }

    /**
     * Authenticates the user into the ATM system by checking the validity of their credentials.
     */
    @PostMapping("/auth")
    public OperationResult authenticate(@RequestParam String accountNumber, @RequestParam String pin) {
        logger.debug("Requesting authentication for account number {}", accountNumber);
        userAuthenticationService.authenticate(accountNumber, pin);
        logger.debug("Authentication grated for user account {}", accountNumber);

        return OperationResult.builder()
                .operationType(OperationType.authentication.name())
                .accountNumber(accountNumber)
                .build();
    }

    /**
     * If the user is authenticated, will fetch his account's details and 'deauthenticate' him.
     * Otherwise, he will not be allowed to carry out the operation.
     */
    @GetMapping("/details")
    public OperationResult getAccountDetails(@RequestParam String accountNumber) {
        logger.debug("Requesting account details for account number {}", accountNumber);

        verifyUserIsAuthenticated(accountNumber);

        UserAccount account = userAccountService.fetchAccountDetails(accountNumber);
        logger.debug("Account details successfully fetched for account number {}, current balance {}, holder name '{}'", accountNumber, account.getBalance(), account.getName());

        deauthenticate(accountNumber);

        return OperationResult.builder()
                .operationType(OperationType.accountDetails.name())
                .accountNumber(accountNumber)
                .currentBalance(account.getBalance())
                .holderName(account.getName())
                .build();
    }

    /**
     * If the user is authenticated, will withdraw the given amount from his account and 'deauthenticate' him.
     * Otherwise, he will not be allowed to carry out the operation.
     */
    @PostMapping("/withdraw")
    public OperationResult withdraw(@RequestParam String accountNumber, @RequestParam Double amount) {
        logger.debug("Requesting withdrawal with amount {} for account number {}", amount, accountNumber);

        verifyUserIsAuthenticated(accountNumber);

        UserAccount account = userAccountService.withdraw(accountNumber, amount);
        logger.debug("Withdrawal successful with amount {} for account number {}, current balance {}", amount, accountNumber, account.getBalance());

        deauthenticate(accountNumber);

        return OperationResult.builder()
                .operationType(OperationType.withdraw.name())
                .operatedAmount(amount)
                .currentBalance(account.getBalance())
                .build();
    }

    /**
     * If the user is authenticated, will deposit the given amount into his account and 'deauthenticate' him.
     * Otherwise, he will not be allowed to carry out the operation.
     */
    @PostMapping("/deposit")
    public OperationResult deposit(@RequestParam String accountNumber, @RequestParam Double amount) {
        logger.debug("Requesting deposit with amount {} for account number {}", amount, accountNumber);

        verifyUserIsAuthenticated(accountNumber);

        UserAccount account = userAccountService.deposit(accountNumber, amount);
        logger.debug("Deposit successful with amount {} for account number {}, current balance {}", amount, accountNumber, account.getBalance());

        deauthenticate(accountNumber);

        return OperationResult.builder()
                .operationType(OperationType.deposit.name())
                .operatedAmount(amount)
                .currentBalance(account.getBalance())
                .build();
    }

    private void verifyUserIsAuthenticated(String accountNumber) {
        userAuthenticationService.verifyUserIsAuthenticated(accountNumber);
        logger.debug("User for the account {} is authenticated, going to carry out his request.", accountNumber);
    }

    private void deauthenticate(String accountNumber) {
        userAuthenticationService.deauthenticate(accountNumber);
        logger.debug("User for the account {} was deauthenticated, since he already carried out one operation.", accountNumber);
    }
}
