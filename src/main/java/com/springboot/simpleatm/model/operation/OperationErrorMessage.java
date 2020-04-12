package com.springboot.simpleatm.model.operation;

/**
 * Error messages shown to the user in the response body if something goes wrong during an operation.
 */
public class OperationErrorMessage {
    public static final String ACCOUNT_NOT_FOUND = "account number not found";
    public static final String INVALID_AMOUNT = "invalid amount, must be nonnull and strictly greater than 0";
    public static final String INSUFFICIENT_BALANCE = "insufficient balance";
}
