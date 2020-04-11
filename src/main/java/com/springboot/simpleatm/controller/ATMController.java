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
@RequestMapping("/userAccount")
public class ATMController {
    Logger logger = LoggerFactory.getLogger(ATMController.class);

    private UserAccountService userAccountService;

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
            UserAccount userAccount = userAccountService.fetchAccountDetails(accountNumber);
            logger.debug("Account details successfully fetched for account number {}", accountNumber);
            return buildSuccessfulResult(userAccount, operationType);
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
            String currency = payload.getCurrency();
            logger.debug("Requesting withdrawal with amount {} in currency {} for account number {}", amount, currency, accountNumber);
            UserAccount userAccount = userAccountService.withdraw(accountNumber, amount);
            logger.debug("Withdrawal successful with amount {} for account number {}", amount, accountNumber);
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

        return buildUnsuccessfulResult(accountNumber, operationType, errorMessage);
    }

    @PostMapping("/{accountNumber}/deposit")
    public OperationResult deposit(@PathVariable String accountNumber, @RequestBody OperationPayload payload) {
        Double amount = payload.getAmount();
        OperationType operationType = OperationType.deposit;
        String errorMessage;

        try {
            String currency = payload.getCurrency();
            logger.debug("Requesting deposit with amount {} in currency {} for account number {}", amount, currency, accountNumber);
            UserAccount userAccount = userAccountService.deposit(accountNumber, amount);
            logger.debug("Deposit successful with amount {} for account number {}", amount, accountNumber);
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

        return buildUnsuccessfulResult(accountNumber, operationType, errorMessage);
    }


    private OperationResult buildSuccessfulResult(UserAccount userAccount, OperationType type) {
        return buildSuccessfulResult(userAccount, type, null);
    }

    private OperationResult buildSuccessfulResult(UserAccount userAccount, OperationType type, Double operatedAmount) {
        return OperationResult.builder()
                .operationType(type.name())
                .operationStatus(OperationStatus.succeeded.name())
                .operatedAmount(operatedAmount)
                .accountNumber(userAccount.getAccountNumber())
                .currentBalance(userAccount.getBalance())
                .holderName(userAccount.getName())
                .build();
    }

    private OperationResult buildUnsuccessfulResult(String accountNumber, OperationType type, String errorMessage) {
        return OperationResult.builder()
                .accountNumber(accountNumber)
                .operationType(type.name())
                .operationStatus(OperationStatus.failed.name())
                .error(errorMessage)
                .build();
    }
}
