package com.dev.vetbackend.services;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String message);
}
