package com.springboot.simpleatm.model.operation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springboot.simpleatm.controller.ATMController;
import lombok.Builder;
import lombok.Data;

/**
 * Object returned as response body by the endpoints defined in the {@link ATMController}.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OperationResult {
    private String operationType;
    private String error;
    private Double operatedAmount;
    private Double currentBalance;
    private String accountNumber;
    private String holderName;
}
