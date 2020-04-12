package com.springboot.simpleatm.model.operation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

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
