package com.springboot.simpleatm.controller;

import com.springboot.simpleatm.account.UserAccountService;
import com.springboot.simpleatm.error.UserAccountNotFoundException;
import com.springboot.simpleatm.model.operation.OperationErrorMessage;
import com.springboot.simpleatm.model.operation.OperationStatus;
import com.springboot.simpleatm.model.operation.OperationType;
import com.springboot.simpleatm.model.operation.UserAccountOperationResult;
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

    @GetMapping("/{accountNumber}/balance")
    public UserAccountOperationResult getAccountDetails(@PathVariable String accountNumber) {
        logger.debug("Requesting account details for account number {}", accountNumber);
        try {
            UserAccount userAccount = userAccountService.fetchAccountDetails(accountNumber);
            logger.debug("Account details successfully for account number {}", accountNumber);
            return buildSuccessfulResult(userAccount, OperationType.accountDetails);
        } catch (UserAccountNotFoundException e) {
            logger.error("Account number {} not found", accountNumber, e);
            return buildUnsuccessfulResult(accountNumber, OperationType.accountDetails, OperationErrorMessage.ACCOUNT_NOT_FOUND);
        } catch (Exception e) {
            logger.error("Could not fetch account details for account number {}", accountNumber, e);
            return buildUnsuccessfulResult(accountNumber, OperationType.accountDetails, OperationErrorMessage.COULD_NOT_PROCESS);
        }
    }

    @PostMapping("/{accountNumber}/withdraw")
    public void withdraw(@PathVariable String accountNumber) {
        logger.debug("withdraw called {}", accountNumber);
    }

    @PostMapping("/{accountNumber}/deposit")
    public void deposit(@PathVariable String accountNumber) {
        logger.debug("deposit called {}", accountNumber);
    }

//    private UserAccountOperationResult buildSuccessfulResult(UserAccount userAccount, OperationType type, double amount) {
//        return UserAccountOperationResult.builder()
//                .accountNumber(userAccount.getAccountNumber())
//                .name(userAccount.getName())
//                .currentBalance(userAccount.getBalance())
//                .operatedAmount(amount)
//                .operationType(type.name())
//                .operationStatus(OperationStatus.succeeded.name())
//                .build();
//    }

    private UserAccountOperationResult buildSuccessfulResult(UserAccount userAccount, OperationType type) {
        return UserAccountOperationResult.builder()
                .accountNumber(userAccount.getAccountNumber())
                .name(userAccount.getName())
                .currentBalance(userAccount.getBalance())
                .operationType(type.name())
                .operationStatus(OperationStatus.succeeded.name())
                .build();
    }

    private UserAccountOperationResult buildUnsuccessfulResult(String accountNumber, OperationType type, String errorMessage) {
        return UserAccountOperationResult.builder()
                .accountNumber(accountNumber)
                .operationType(type.name())
                .operationStatus(OperationStatus.failed.name())
                .error(errorMessage)
                .build();
    }
}
