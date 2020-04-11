package com.springboot.simpleatm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    @Column(name = "card_number")
    private String cardNumber;
    @Column(name = "pin_hash")
    private String pinHash;
    private Double balance;

    public Account(String name, String cardNumber, String pinHash, Double balance) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.pinHash = pinHash;
        this.balance = balance;
    }
}
