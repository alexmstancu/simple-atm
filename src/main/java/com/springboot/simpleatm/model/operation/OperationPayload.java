package com.springboot.simpleatm.model.operation;

import lombok.Data;

@Data
public class OperationPayload {
    private Double amount;
    /**
     * Unused per se, but it's an idea for improvements (exchanges)
     */
    private String currency;
}
