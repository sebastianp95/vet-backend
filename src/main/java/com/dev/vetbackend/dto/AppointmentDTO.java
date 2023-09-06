package com.dev.vetbackend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentDTO {
    private String petName;
    private String reason;
    private String phoneNumber;
    private boolean sendSMS;
    private String email;
    private boolean sendEmail;
    private String message;
    private String petOwnerName;
    private LocalDateTime date;
    private String language;
}
