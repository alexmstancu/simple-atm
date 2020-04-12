package com.springboot.simpleatm.model.security;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.Instant;

@Data
@Builder
public class Token {
    private String value;
    private Duration duration;
    private Instant creationTime;
}
