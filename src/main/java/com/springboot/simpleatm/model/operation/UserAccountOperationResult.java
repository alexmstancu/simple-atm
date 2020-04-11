package com.springboot.simpleatm.model.operation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAccountOperationResult {
    private String accountNumber;
    private String name;
    private Double currentBalance;
    private Double operatedAmount;
    private String operationType;
    private String operationStatus;
    private String error;
}
