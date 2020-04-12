package com.springboot.simpleatm.controller;

import com.springboot.simpleatm.exception.InsufficientBalanceException;
import com.springboot.simpleatm.exception.InvalidAmountException;
import com.springboot.simpleatm.exception.UserAccountNotFoundException;
import com.springboot.simpleatm.model.operation.OperationErrorMessage;
import com.springboot.simpleatm.model.operation.OperationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestControllerExceptionHandler.class);

    @ExceptionHandler(UserAccountNotFoundException.class)
    public ResponseEntity<OperationResult> handleUserNotFound(UserAccountNotFoundException e) {
        String accountNumber = e.getAccountNumber();

        logger.error("Account number {} not found.", accountNumber, e);

        OperationResult operationResult = OperationResult.builder()
                .error(OperationErrorMessage.ACCOUNT_NOT_FOUND)
                .accountNumber(accountNumber)
                .build();

        return new ResponseEntity<>(operationResult, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<OperationResult> handleInvalidAmount(InvalidAmountException e) {
        Double amount = e.getAmount();

        logger.error("Invalid amount '{}' for the requested operation.", amount, e);

        OperationResult operationResult = OperationResult.builder()
                .error(OperationErrorMessage.INVALID_AMOUNT)
                .operatedAmount(amount)
                .build();

        return new ResponseEntity<>(operationResult, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<OperationResult> handleInsufficientBalance(InsufficientBalanceException e) {
        Double amount = e.getAmount();
        Double currentBalance = e.getCurrentBalance();

        logger.error("Balance {} insufficient for the requested operation with amount {}.", currentBalance, amount, e);

        OperationResult operationResult = OperationResult.builder()
                .error(OperationErrorMessage.INSUFFICIENT_BALANCE)
                .operatedAmount(amount)
                .currentBalance(currentBalance)
                .build();

        return new ResponseEntity<>(operationResult, HttpStatus.BAD_REQUEST);
    }
}
