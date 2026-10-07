package com.mflores.telecomapp.tests.phoneline.service;

import com.mflores.telecomapp.service.PhoneNumberGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace =  AutoConfigureTestDatabase.Replace.NONE)
@Import(PhoneNumberGenerator.class)

public class PhoneNumberGeneratorTest {

    @Autowired
    private PhoneNumberGenerator phoneNumberGenerator;

    @Test
    void shouldGeneratePhoneNumber() {

        String generatedPhoneNumber = phoneNumberGenerator.generatePhoneNumber();

        assertNotNull(generatedPhoneNumber);
    }

    @Test
    void shouldGeneratePhoneNumberWithCorrespondingPrefix() {

        String generatedPhoneNumber = phoneNumberGenerator.generatePhoneNumber();
        assertEquals("2125", generatedPhoneNumber.substring(0,4));
    }

    @Test
    void shouldGenerateTwoDifferentPhoneNumbersWhenPhoneNumberGeneratorIsCalledMoreThanOnce() {
        String generatedPhoneNumber = phoneNumberGenerator.generatePhoneNumber();
        String generatedPhoneNumber2 = phoneNumberGenerator.generatePhoneNumber();

        assertNotEquals(generatedPhoneNumber, generatedPhoneNumber2);
    }

    @Test
    void shouldGenerateATenPhoneNumber() {
        String generatedPhoneNumber = phoneNumberGenerator.generatePhoneNumber();

        assertEquals(10, generatedPhoneNumber.length());
    }
}
