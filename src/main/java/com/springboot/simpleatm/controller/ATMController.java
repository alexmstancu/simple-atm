package com.springboot.simpleatm.controller;

import com.springboot.simpleatm.account.UserAccountService;
import com.springboot.simpleatm.exception.InsufficientBalanceException;
import com.springboot.simpleatm.exception.InvalidAmountException;
import com.springboot.simpleatm.exception.UserAccountNotFoundException;
import com.springboot.simpleatm.model.operation.*;
import com.springboot.simpleatm.model.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public OperationResult getAccountDetails(@PathVariable String accountNumber) {
        OperationType operationType = OperationType.accountDetails;
        String errorMessage;

        try {
            logger.debug("Requesting account details for account number {}", accountNumber);
            UserAccount account = userAccountService.fetchAccountDetails(accountNumber);
            logger.debug("Account details successfully fetched for account number {}, current balance {}, holder name '{}'", accountNumber, account.getBalance(), account.getName());
            return buildSuccessfulResult(account, operationType);
        } catch (UserAccountNotFoundException e) {
            logger.error("Account number {} not found", accountNumber, e);
            errorMessage = OperationErrorMessage.ACCOUNT_NOT_FOUND;
        } catch (Exception e) {
            logger.error("Could not fetch account details for account number {}", accountNumber, e);
            errorMessage = OperationErrorMessage.COULD_NOT_PROCESS;
        }

        return buildUnsuccessfulResult(accountNumber, operationType, errorMessage);
    }

    @PostMapping("/{accountNumber}/withdraw")
    public OperationResult withdraw(@PathVariable String accountNumber, @RequestBody OperationPayload payload) {
        Double amount = payload.getAmount();
        OperationType operationType = OperationType.withdraw;
        String errorMessage;

        try {
            logger.debug("Requesting withdrawal with amount {} in currency {} for account number {}", amount, payload.getCurrency(), accountNumber);
            UserAccount userAccount = userAccountService.withdraw(accountNumber, amount);
            logger.debug("Withdrawal successful with amount {} for account number {}, current balance {}", amount, accountNumber, userAccount.getBalance());
            return buildSuccessfulResult(userAccount, operationType, amount);
        } catch (UserAccountNotFoundException e) {
            logger.error("Account number {} not found", accountNumber, e);
            errorMessage = OperationErrorMessage.ACCOUNT_NOT_FOUND;
        } catch (InvalidAmountException e) {
            logger.error("Invalid amount '{}' for withdrawal operation for account number {}", amount, accountNumber, e);
            errorMessage = OperationErrorMessage.INVALID_AMOUNT;
        } catch (InsufficientBalanceException e) {
            logger.error("Insufficient balance for withdrawal operation with amount {} for account number {}", amount, accountNumber, e);
            errorMessage = OperationErrorMessage.INSUFFICIENT_BALANCE;
        }  catch (Exception e) {
            logger.error("Could process withdrawal of amount {} from account number {}", amount, accountNumber, e);
            errorMessage = OperationErrorMessage.COULD_NOT_PROCESS;
        }

        return buildUnsuccessfulResult(accountNumber, operationType, amount, errorMessage);
    }

    @PostMapping("/{accountNumber}/deposit")
    public OperationResult deposit(@PathVariable String accountNumber, @RequestBody OperationPayload payload) {
        Double amount = payload.getAmount();
        OperationType operationType = OperationType.deposit;
        String errorMessage;

        try {
            logger.debug("Requesting deposit with amount {} in currency {} for account number {}", amount, payload.getCurrency(), accountNumber);
            UserAccount userAccount = userAccountService.deposit(accountNumber, amount);
            logger.debug("Deposit successful with amount {} for account number {}, current balance {}", amount, accountNumber, userAccount.getBalance());
            return buildSuccessfulResult(userAccount, operationType, amount);
        } catch (UserAccountNotFoundException e) {
            logger.error("Account number {} not found", accountNumber, e);
            errorMessage = OperationErrorMessage.ACCOUNT_NOT_FOUND;
        } catch (InvalidAmountException e) {
            logger.error("Invalid amount '{}' for deposit operation for account number {}", amount, accountNumber, e);
            errorMessage = OperationErrorMessage.INVALID_AMOUNT;
        } catch (Exception e) {
            logger.error("Could process withdrawal of amount {} from account number {}", amount, accountNumber, e);
            errorMessage = OperationErrorMessage.COULD_NOT_PROCESS;
        }

        return buildUnsuccessfulResult(accountNumber, operationType, amount, errorMessage);
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
     * Creates an unsuccessful {@link OperationResult} object having the following fields set:
     * - operationType (see {@link OperationType}
     * - operationStatus (equal to {@link OperationStatus#failed})
     * - error (see {@link OperationErrorMessage}
     * - accountNumber
     */
    private OperationResult buildUnsuccessfulResult(String accountNumber, OperationType type, String errorMessage) {
        return buildResult(accountNumber, null, type, null, OperationStatus.failed, null, errorMessage);
    }

    /**
     * Creates an unsuccessful {@link OperationResult} object having the following fields set:
     * - operationType (see {@link OperationType}
     * - operationStatus (equal to {@link OperationStatus#failed})
     * - error (see {@link OperationErrorMessage}
     * - operatedAmount
     * - accountNumber
     */
    private OperationResult buildUnsuccessfulResult(String accountNumber, OperationType type, Double operatedAmount, String errorMessage) {
        return buildResult(accountNumber, null, type, operatedAmount, OperationStatus.failed, null, errorMessage);
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
