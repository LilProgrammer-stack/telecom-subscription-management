package com.mflores.telecomapp.service;

import com.mflores.telecomapp.config.EmailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final EmailProperties emailProperties;
    private final JavaMailSender mailSender;

    public EmailService(EmailProperties emailProperties,  JavaMailSender mailSender) {
        this.emailProperties = emailProperties;
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String recipient, String verificationLink){

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(emailProperties.getFromAddress());
        message.setTo(recipient);
        message.setSubject("Verify your TelecomApp email");
        message.setText(
                "Welcome to TelecomApp!\n\n"
                        + "Please verify your email by opening the following link:\n"
                        + verificationLink
                        + "\n\nThis link will expire shortly."
        );

        mailSender.send(message);
    }

    public String getVerificationBaseUrl() {
        return emailProperties.getVerificationBaseUrl();
    }
}
