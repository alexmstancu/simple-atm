package com.springboot.simpleatm.controller;

import com.springboot.simpleatm.exception.*;
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
        logger.error("Account number '{}' was not found in the system for the requested operation.", e.getAccountNumber(), e);
        return buildErrorResult(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<OperationResult> handleInvalidAmount(InvalidAmountException e) {
        logger.error("Invalid amount for the requested operation for accountNumber {}.", e.getAccountNumber(), e);
        return buildErrorResult(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<OperationResult> handleInsufficientBalance(InsufficientBalanceException e) {
        logger.error("Balance insufficient for the requested operation for account {}.", e.getAccountNumber(), e);
        return buildErrorResult(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserAlreadyAuthenticatedException.class, UserAccountPinIncorrectException.class, UserUnauthenticatedException.class})
    public ResponseEntity<OperationResult> handleSecurity(UserAccountOperationException e) {
        logger.error("User for account number {} could not be authenticated for the requested operation: ", e.getAccountNumber(), e);
        return buildErrorResult(e, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<OperationResult> buildErrorResult(UserAccountOperationException e, HttpStatus status) {
        OperationResult operationResult = OperationResult.builder()
                .error(e.getMessage())
                .accountNumber(e.getAccountNumber())
                .build();

        return new ResponseEntity<>(operationResult, status);
    }
}
