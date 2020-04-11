package com.springboot.simpleatm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ATMController {
    Logger logger = LoggerFactory.getLogger(ATMController.class);

    public void login(String cardNumber, String pin) {

    }

    @GetMapping
    public void getBalance() {

    }

    @PostMapping
    public void withdraw() {

    }

    @PostMapping
    public void deposit() {

    }
}
