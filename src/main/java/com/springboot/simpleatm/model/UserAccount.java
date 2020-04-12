package com.springboot.simpleatm.model;

import lombok.Data;

import javax.persistence.*;

/**
 * The model stored in the DB (this is the only DB entity).
 */
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
    private String pin;
    private Double balance;
}
