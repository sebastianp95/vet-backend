package com.dev.vetbackend.services;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String message);
    void sendHtmlEmail(String to, String subject, String htmlMessage) ;
}
