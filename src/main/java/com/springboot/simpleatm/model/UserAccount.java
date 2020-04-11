package com.springboot.simpleatm.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_account")
@Data
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "pin_hash")
    private String pinHash;
    private Double balance;
}
