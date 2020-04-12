package com.springboot.simpleatm.controller;

import com.springboot.simpleatm.account.UserAccountService;
import com.springboot.simpleatm.model.UserAccount;
import com.springboot.simpleatm.model.operation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/atm/userAccount")
public class ATMController {
    private static final Logger logger = LoggerFactory.getLogger(ATMController.class);

    private final UserAccountService userAccountService;

    @Autowired
    public ATMController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    public void login(String cardNumber, String pin) {
        // TODO
    }

    @GetMapping("/{accountNumber}/details")
    public ResponseEntity<OperationResult> getAccountDetails(@PathVariable String accountNumber) {
        logger.debug("Requesting account details for account number {}", accountNumber);
        UserAccount account = userAccountService.fetchAccountDetails(accountNumber);
        logger.debug("Account details successfully fetched for account number {}, current balance {}, holder name '{}'", accountNumber, account.getBalance(), account.getName());

        OperationResult operationResult = buildSuccessfulResult(account, OperationType.accountDetails);
        return ResponseEntity.ok(operationResult);
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<OperationResult> withdraw(@PathVariable String accountNumber, @RequestBody OperationPayload payload) {
        Double amount = payload.getAmount();

        logger.debug("Requesting withdrawal with amount {} in currency {} for account number {}", amount, payload.getCurrency(), accountNumber);
        UserAccount userAccount = userAccountService.withdraw(accountNumber, amount);
        logger.debug("Withdrawal successful with amount {} for account number {}, current balance {}", amount, accountNumber, userAccount.getBalance());

        OperationResult operationResult = buildSuccessfulResult(userAccount, OperationType.withdraw, amount);
        return ResponseEntity.ok(operationResult);

    }

    @PostMapping("/{accountNumber}/deposit")
    public OperationResult deposit(@PathVariable String accountNumber, @RequestBody OperationPayload payload) {
        Double amount = payload.getAmount();

        logger.debug("Requesting deposit with amount {} in currency {} for account number {}", amount, payload.getCurrency(), accountNumber);
        UserAccount userAccount = userAccountService.deposit(accountNumber, amount);
        logger.debug("Deposit successful with amount {} for account number {}, current balance {}", amount, accountNumber, userAccount.getBalance());

        return buildSuccessfulResult(userAccount, OperationType.deposit, amount);
    }

    /**
     * Creates a successful {@link OperationResult} object having the following fields set:
     * - operationType (see {@link OperationType}
     * - operationStatus (equal to {@link OperationStatus#succeeded})
     * - currentBalance
     * - accountNumber
     * - holderName
     */
    private OperationResult buildSuccessfulResult(UserAccount account, OperationType type) {
        return buildResult(account.getAccountNumber(), account.getBalance(), type, null, OperationStatus.succeeded, account.getName(), null);
    }

    /**
     * Creates a successful {@link OperationResult} object having the following fields populated from the given parameters:
     * - operationType (see {@link OperationType}
     * - operationStatus (equal to {@link OperationStatus#succeeded})
     * - operatedAmount
     * - currentBalance
     * - accountNumber
     */
    private OperationResult buildSuccessfulResult(UserAccount account, OperationType type, Double operatedAmount) {
        return buildResult(account.getAccountNumber(), account.getBalance(), type, operatedAmount, OperationStatus.succeeded, null, null);

    }

    /**
     * Creates an {@link OperationResult} object having all the fields set with the given parameters.
     * See {@link OperationType}, {@link OperationStatus}, {@link OperationErrorMessage}
     */
    private OperationResult buildResult(String accountNumber, Double balance, OperationType type, Double operatedAmount, OperationStatus status, String name, String errorMessage) {
        return OperationResult.builder()
                .operationType(type.name())
                .operationStatus(status.name())
                .operatedAmount(operatedAmount)
                .accountNumber(accountNumber)
                .currentBalance(balance)
                .holderName(name)
                .error(errorMessage)
                .build();
    }
}
