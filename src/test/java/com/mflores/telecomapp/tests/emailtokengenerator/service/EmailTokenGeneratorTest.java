package com.mflores.telecomapp.tests.emailtokengenerator.service;

import com.mflores.telecomapp.service.SecureTokenGenerator;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EmailTokenGeneratorTest {

    @Test
    void shouldGenerateTwoDistinctEmailTokens() {

        SecureTokenGenerator secureTokenGenerator = new SecureTokenGenerator();
        String token1 = secureTokenGenerator.generateEmailToken();
        String token2 = secureTokenGenerator.generateEmailToken();

        assertNotEquals(token1, token2);
    }

    @Test
    void shouldGenerateUrlSafeBase64Token() {

        SecureTokenGenerator secureTokenGenerator = new SecureTokenGenerator();
        String token = secureTokenGenerator.generateEmailToken();

        assertTrue(token.matches("[A-Za-z0-9_-]+"));
    }

    @Test
    void shouldGenerateTokenWithExpectedEncodedLength() {

        SecureTokenGenerator secureTokenGenerator = new SecureTokenGenerator();
        String token = secureTokenGenerator.generateEmailToken();

        assertEquals(43, token.length());
    }
}
