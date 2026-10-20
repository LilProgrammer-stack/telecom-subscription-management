package com.mflores.telecomapp.tests.email.service;

import com.mflores.telecomapp.config.EmailProperties;
import com.mflores.telecomapp.service.EmailService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class EmailServiceTest {

    @Test
    void shouldSendVerificationEmailToMailpit() {
        EmailProperties properties = new EmailProperties();
        properties.setVerificationBaseUrl("http://localhost:8080");
        properties.setFromAddress("no-reply@telecomapp.local");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("localhost");
        mailSender.setPort(1025);

        EmailService emailService = new EmailService(properties, mailSender);

        assertDoesNotThrow(() ->
                emailService.sendVerificationEmail(
                        "customer@example.com",
                        "http://localhost:8080/api/registration/verify?token=test-token"
                )
        );
    }

}
