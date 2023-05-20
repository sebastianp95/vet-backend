package com.dev.vetbackend.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    @Value("${twilio.auth.token}")
    private String AUTH_TOKEN;

    @Value("${twilio.account.sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.phone.number}")
    private String FROM_NUMBER;
    @PostConstruct
    public void init() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public String sendMessage(String to, String body) {
        try {
            Message message = Message.creator(
                            new PhoneNumber(to),  // Destination phone number
                            new PhoneNumber(FROM_NUMBER), // Twilio phone number
                            body)
                    .create();

            System.out.println("SMS sent with SID: " + message.getSid());

            return message.getSid();
        } catch (Exception e) {
            // Log the exception if necessary
            System.err.println("Error sending message: " + e.getMessage());
            return null; // or throw a custom exception, return a specific error code, etc.
        }
    }


}
