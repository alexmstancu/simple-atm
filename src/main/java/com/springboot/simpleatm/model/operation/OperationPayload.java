package com.springboot.simpleatm.model.operation;

import com.springboot.simpleatm.controller.ATMController;
import lombok.Data;

/**
 * Very simple object that the client of the endpoint has to use as request body when calling
 * {@link ATMController#withdraw(String, OperationPayload)} or {@link ATMController#deposit(String, OperationPayload)}.
 */
@Data
public class OperationPayload {
    private Double amount;
}
