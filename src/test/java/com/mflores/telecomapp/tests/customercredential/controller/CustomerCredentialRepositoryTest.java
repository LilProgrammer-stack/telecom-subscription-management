package com.mflores.telecomapp.tests.customercredential.controller;

import com.mflores.telecomapp.model.AccountOwner;
import com.mflores.telecomapp.model.CustomerCredential;
import com.mflores.telecomapp.repository.AccountOwnerRepository;
import com.mflores.telecomapp.repository.CustomerCredentialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class CustomerCredentialRepositoryTest{

    @Autowired
    private CustomerCredentialRepository customerCredentialRepository;
    @Autowired
    private AccountOwnerRepository accountOwnerRepository;

    private AccountOwner accountOwner;

    @BeforeEach
    void setup() {
        accountOwner = new AccountOwner(
                "John",
                "Smith",
                "john.smith@example.com",
                LocalDate.of(1995, 4, 20)
        );

        accountOwnerRepository.save(accountOwner);
    }

    @Test
    void shouldSaveCustomerCredentialSuccessfully() {

        CustomerCredential customerCredential = new CustomerCredential();
        customerCredential.setPasswordHash("aPassword");
        customerCredential.setAccountOwner(accountOwner);
        customerCredential.setEmailVerified(false);

        CustomerCredential savedCredential =
                customerCredentialRepository.saveAndFlush(customerCredential);

        assertNotNull(savedCredential.getCredentialId());
        assertEquals(accountOwner, savedCredential.getAccountOwner());
        assertEquals("aPassword", savedCredential.getPasswordHash());
        assertFalse(savedCredential.getEmailVerified());
    }

    @Test
    void shouldThrowExceptionWhenPasswordHashIsNull() {

        CustomerCredential customerCredential = new CustomerCredential();
        customerCredential.setPasswordHash(null);
        customerCredential.setAccountOwner(accountOwner);
        customerCredential.setEmailVerified(true);

        assertThrows(DataIntegrityViolationException.class,
                () -> customerCredentialRepository.saveAndFlush(customerCredential));
    }

    @Test
    void shouldThrowExceptionWhenEmailVerifiedIsNull() {

        CustomerCredential customerCredential = new CustomerCredential();
        customerCredential.setPasswordHash("aPassword");
        customerCredential.setAccountOwner(accountOwner);
        customerCredential.setEmailVerified(null);

        assertThrows(DataIntegrityViolationException.class,
                () -> customerCredentialRepository.saveAndFlush(customerCredential));
    }

    @Test
    void shouldThrowExceptionWhenAccountOwnerIsNull() {

        CustomerCredential customerCredential = new CustomerCredential();
        customerCredential.setPasswordHash("aPassword");
        customerCredential.setAccountOwner(null);
        customerCredential.setEmailVerified(true);

        assertThrows(DataIntegrityViolationException.class,
                () -> customerCredentialRepository.saveAndFlush(customerCredential));
    }

    @Test
    void shouldNotAllowDuplicatedAccountOwnerId() {

        CustomerCredential customerCredential = new CustomerCredential();
        customerCredential.setPasswordHash("aPassword");
        customerCredential.setAccountOwner(accountOwner);
        customerCredential.setEmailVerified(true);

        customerCredentialRepository.saveAndFlush(customerCredential);

        CustomerCredential customerCredential2 = new CustomerCredential();
        customerCredential2.setPasswordHash("aPassword");
        customerCredential2.setAccountOwner(accountOwner);
        customerCredential2.setEmailVerified(true);

        assertThrows(DataIntegrityViolationException.class,
                () -> customerCredentialRepository.saveAndFlush(customerCredential2));
    }
}
