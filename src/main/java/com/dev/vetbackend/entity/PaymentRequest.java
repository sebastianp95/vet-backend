package com.dev.vetbackend.entity;

import lombok.Data;

@Data
public class PaymentRequest {
    private String token;
    private int amount;
    private String currency;
}
