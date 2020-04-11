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
    @Column(name = "card_number")
    private String accountNumber;
    @Column(name = "pin_hash")
    private String pinHash;
    private BigDecimal balance;

    public UserAccount(String name, String accountNumber, String pinHash, BigDecimal balance) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.pinHash = pinHash;
        this.balance = balance;
    }
}
