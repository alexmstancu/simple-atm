package com.springboot.simpleatm.model.operation;

import com.springboot.simpleatm.controller.ATMController;

/**
 * The type of the operations carried out by the {@link ATMController} endpoint.
 */
public enum OperationType {
    accountDetails,
    deposit,
    withdraw
}
