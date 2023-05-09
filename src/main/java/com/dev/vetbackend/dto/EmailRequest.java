package com.dev.vetbackend.dto;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
@Data
public class EmailRequest {
    @NotBlank(message = "Recipient email is required.")
    @Email(message = "Invalid email format.")
    private String recipientEmail;
    @NotBlank(message = "OTP is required.")
    private String OTP;
}

