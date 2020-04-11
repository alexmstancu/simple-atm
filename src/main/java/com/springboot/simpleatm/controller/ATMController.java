package com.springboot.simpleatm.controller;

import com.springboot.simpleatm.account.UserAccountService;
import com.springboot.simpleatm.error.UserAccountNotFoundException;
import com.springboot.simpleatm.model.UserAccountOperationResult;
import com.springboot.simpleatm.model.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/userAccount")
public class ATMController {
    Logger logger = LoggerFactory.getLogger(ATMController.class);

    private UserAccountService userAccountService;

    public void login(String cardNumber, String pin) {
        // TODO
    }

    @GetMapping("/{accountNumber}/balance")
    public UserAccountOperationResult getAccountDetails(@PathVariable String accountNumber) {
        logger.debug("getBalance called {}", accountNumber);
        UserAccount userAccount = null;
        try {
            userAccount = userAccountService.getAccountDetails(accountNumber);
        } catch (UserAccountNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }

    @PostMapping("/{accountNumber}/withdraw")
    public void withdraw(@PathVariable String accountNumber) {
        logger.debug("withdraw called {}", accountNumber);
    }

    @PostMapping("/{accountNumber}/deposit")
    public void deposit(@PathVariable String accountNumber) {
        logger.debug("deposit called {}", accountNumber);
    }

    private UserAccountOperationResult getResult(UserAccount userAccount, String operationDescription, double amount) {
        return UserAccountOperationResult.builder()
                .accountNumber(userAccount.getAccountNumber())
                .name(userAccount.getName())
                .currentBalance(userAccount.getBalance())
                .operatedAmount(amount)
                .operationDescription(operationDescription)
                .build();

    }
}
