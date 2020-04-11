package com.springboot.simpleatm.controller;

import com.springboot.simpleatm.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/")
public class ATMController {
    Logger logger = LoggerFactory.getLogger(ATMController.class);

    private AccountService accountService;

    public void login(String cardNumber, String pin) {

    }

    @GetMapping("/balance")
    public void getBalance() {

    }

    @PostMapping("/withdraw")
    public void withdraw() {

    }

    @PostMapping("deposit")
    public void deposit() {

    }
}
