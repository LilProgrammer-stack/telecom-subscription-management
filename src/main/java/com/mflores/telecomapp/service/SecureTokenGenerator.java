package com.mflores.telecomapp.service;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class SecureTokenGenerator {

    private final SecureRandom random = new SecureRandom();

    public String generateEmailToken() {

        byte[] randomBytes = new byte[32];
        random.nextBytes(randomBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);
    }
}
