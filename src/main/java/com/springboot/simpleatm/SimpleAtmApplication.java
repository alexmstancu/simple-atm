package com.springboot.simpleatm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class SimpleAtmApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimpleAtmApplication.class, args);
    }
}
