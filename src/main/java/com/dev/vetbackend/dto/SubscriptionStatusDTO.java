package com.dev.vetbackend.dto;


import lombok.Data;

@Data
public class SubscriptionStatusDTO {

    private String planId;
    private String status;
    private String subscriptionId;
}
