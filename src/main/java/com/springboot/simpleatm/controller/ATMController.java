package com.springboot.simpleatm.controller;

import com.springboot.simpleatm.account.UserAccountService;
import com.springboot.simpleatm.model.UserAccount;
import com.springboot.simpleatm.model.operation.OperationPayload;
import com.springboot.simpleatm.model.operation.OperationResult;
import com.springboot.simpleatm.model.operation.OperationType;
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
        logger.debug("Requesting account details for account number {}", accountNumber);
        UserAccount account = userAccountService.fetchAccountDetails(accountNumber);
        logger.debug("Account details successfully fetched for account number {}, current balance {}, holder name '{}'", accountNumber, account.getBalance(), account.getName());

        return OperationResult.builder()
                .operationType(OperationType.accountDetails.name())
                .accountNumber(accountNumber)
                .currentBalance(account.getBalance())
                .holderName(account.getName())
                .build();
    }

    @PostMapping("/{accountNumber}/withdraw")
    public OperationResult withdraw(@PathVariable String accountNumber, @RequestBody OperationPayload payload) {
        Double amount = payload.getAmount();

        logger.debug("Requesting withdrawal with amount {} in currency {} for account number {}", amount, payload.getCurrency(), accountNumber);
        UserAccount account = userAccountService.withdraw(accountNumber, amount);
        logger.debug("Withdrawal successful with amount {} for account number {}, current balance {}", amount, accountNumber, account.getBalance());

        return OperationResult.builder()
                .operationType(OperationType.withdraw.name())
                .operatedAmount(amount)
                .currentBalance(account.getBalance())
                .build();
    }

    @PostMapping("/{accountNumber}/deposit")
    public OperationResult deposit(@PathVariable String accountNumber, @RequestBody OperationPayload payload) {
        Double amount = payload.getAmount();

        logger.debug("Requesting deposit with amount {} in currency {} for account number {}", amount, payload.getCurrency(), accountNumber);
        UserAccount account = userAccountService.deposit(accountNumber, amount);
        logger.debug("Deposit successful with amount {} for account number {}, current balance {}", amount, accountNumber, account.getBalance());

        return OperationResult.builder()
                .operationType(OperationType.deposit.name())
                .operatedAmount(amount)
                .currentBalance(account.getBalance())
                .build();
    }
}
