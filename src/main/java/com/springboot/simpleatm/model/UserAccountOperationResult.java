package com.springboot.simpleatm.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAccountOperationResult {
    private String accountNumber;
    private String name;
    private Double currentBalance;
    private Double operatedAmount;
    private String operationDescription;
    private String operationStatus;
}
