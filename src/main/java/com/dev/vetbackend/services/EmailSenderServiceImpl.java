package com.dev.vetbackend.services;

import com.dev.vetbackend.exception.CustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;



@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Environment env;
    @Override
    public void sendEmail(String to, String subject, String message) {

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(env.getProperty("spring.mail.username"));
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(message);

            mailSender.send(simpleMailMessage);

        } catch (Exception e) {
            throw new CustomException("Failed to send email");
        }

    }

    public void sendHtmlEmail(String to, String subject, String htmlMessage) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(env.getProperty("spring.mail.username"));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlMessage, true);  // Set to true to indicate that the text is HTML

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new CustomException("Failed to send HTML email" + e.getMessage());
        }
    }
}