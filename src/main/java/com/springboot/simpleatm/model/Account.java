package com.springboot.simpleatm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter @Setter @NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    @Column(name = "card_number")
    private String accountNumber;
    @Column(name = "pin_hash")
    private String pinHash;
    private BigDecimal balance;

    public Account(String name, String accountNumber, String pinHash, BigDecimal balance) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.pinHash = pinHash;
        this.balance = balance;
    }
}
