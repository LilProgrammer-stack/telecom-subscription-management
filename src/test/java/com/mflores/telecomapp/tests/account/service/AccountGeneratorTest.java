package com.mflores.telecomapp.tests.account.service;

import com.mflores.telecomapp.service.AccountNumberGenerator;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

@Import(AccountNumberGenerator.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountGeneratorTest {

    @Autowired
    private AccountNumberGenerator accountNumberGenerator;

    @Test
    void shouldReturnAccountNumberWhenNumberGeneratorIsCalled(){

        String generatedSequenceNumber = accountNumberGenerator.generate();
        assertNotNull(generatedSequenceNumber);

    }

    @Test
    void shouldReturnTbPrefixAtTheBeginningOfAccountNumberWhenNumberGeneratorIsCalled(){

        String generatedNumber = accountNumberGenerator.generate();
        String tb = generatedNumber.substring(0,2);

        assertEquals("TB",tb);
    }

    @Test
    void shouldGenerateDifferentAccountNumbersIfNumberGeneratorIsCalledMoreThanOnce(){

        String generatedNumber = accountNumberGenerator.generate();
        String secondGeneratedNumber = accountNumberGenerator.generate();

        assertNotEquals(generatedNumber,secondGeneratedNumber);
    }
}
