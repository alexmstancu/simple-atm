package com.springboot.simpleatm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
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
