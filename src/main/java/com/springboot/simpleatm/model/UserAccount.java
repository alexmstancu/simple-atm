package com.springboot.simpleatm.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "user_account")
@Data
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    private String accountNumber;
    private String pinHash;
    private Double balance;

    public UserAccount(String name, String accountNumber, String pinHash, Double balance) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.pinHash = pinHash;
        this.balance = balance;
    }
}
