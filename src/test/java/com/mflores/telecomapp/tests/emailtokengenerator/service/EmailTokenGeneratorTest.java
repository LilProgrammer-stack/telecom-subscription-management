package com.mflores.telecomapp.tests.emailtokengenerator.service;

import com.mflores.telecomapp.service.EmailTokenGenerator;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EmailTokenGeneratorTest {

    @Test
    void shouldGenerateTwoDistinctEmailTokens() {

        EmailTokenGenerator emailTokenGenerator = new EmailTokenGenerator();
        String token1 = emailTokenGenerator.generateEmailToken();
        String token2 = emailTokenGenerator.generateEmailToken();

        assertNotEquals(token1, token2);
    }

    @Test
    void shouldGenerateUrlSafeBase64Token() {

        EmailTokenGenerator emailTokenGenerator = new EmailTokenGenerator();
        String token = emailTokenGenerator.generateEmailToken();

        assertTrue(token.matches("[A-Za-z0-9_-]+"));
    }

    @Test
    void shouldGenerateTokenWithExpectedEncodedLength() {

        EmailTokenGenerator emailTokenGenerator = new EmailTokenGenerator();
        String token = emailTokenGenerator.generateEmailToken();

        assertEquals(43, token.length());
    }
}
