package com.mflores.telecomapp.tests.customerregistration.service;

import com.mflores.telecomapp.dto.*;
import com.mflores.telecomapp.model.AccountOwner;
import com.mflores.telecomapp.model.BillingLanguage;
import com.mflores.telecomapp.model.CustomerCredential;
import com.mflores.telecomapp.repository.AccountOwnerRepository;
import com.mflores.telecomapp.repository.CustomerCredentialRepository;
import com.mflores.telecomapp.service.AccountService;
import com.mflores.telecomapp.service.CustomerRegistrationService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerRegistrationServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AccountOwnerRepository accountOwnerRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private CustomerCredentialRepository  customerCredentialRepository;

    @InjectMocks
    private CustomerRegistrationService customerRegistrationService;

    private OffsetDateTime createdAt = OffsetDateTime.of(2025, 10, 30, 0, 0, 0, 0, ZoneOffset.UTC);
    private AccountOwner accountOwner = new AccountOwner();
    private CustomerCredential customerCredential = new CustomerCredential();
    private AccountResponse  accountResponse = new AccountResponse();

    @BeforeEach
    public void setUp() {
        accountOwner.setFirstName("John");
        accountOwner.setLastName("Doe");
        accountOwner.setCreatedAt(createdAt);
        accountOwner.setEmail("doejohn10@gmail.com");
        accountOwner.setDateOfBirth(LocalDate.of(1980, 1, 1));

        customerCredential.setAccountOwner(accountOwner);
        customerCredential.setPasswordHash("passwordtest");
        customerCredential.setEmailVerified(false);

        accountResponse.setAccountNumber("TB123456");
    }

    @Test
    void shouldBuildCustomerRegistrationSuccessfully(){

        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setBillingLanguage(BillingLanguage.ENGLISH);

        CreateCustomerRegistrationRequest  createCustomerRegistrationRequest = new CreateCustomerRegistrationRequest();
        createCustomerRegistrationRequest.setFirstName("John");
        createCustomerRegistrationRequest.setLastName("Doe");
        createCustomerRegistrationRequest.setEmail("doejohn10@gmail.com");
        createCustomerRegistrationRequest.setDateOfBirth(LocalDate.of(1980, 1, 1));

        CreateCustomerCredentialRequest  createCustomerCredentialRequest = new CreateCustomerCredentialRequest();
        createCustomerCredentialRequest.setPassword("Password123!");
        createCustomerCredentialRequest.setConfirmPassword("Password123!");

        when(accountOwnerRepository.save(any(AccountOwner.class)))
                .thenReturn(accountOwner);

        when(passwordEncoder.encode(createCustomerCredentialRequest.getPassword()))
                .thenReturn("passwordtest");

        when(customerCredentialRepository.save(any(CustomerCredential.class)))
                .thenReturn(customerCredential);

        when(accountService.createAccount(createAccountRequest, accountOwner))
                .thenReturn(accountResponse);

        CustomerRegistrationResponse customerRegistrationResponse = customerRegistrationService.createCustomerRegistration(createAccountRequest, createCustomerRegistrationRequest,
                createCustomerCredentialRequest);

        assertEquals("TB123456",customerRegistrationResponse.getAccountNumber());
        assertEquals("John",customerRegistrationResponse.getFirstName());
        assertEquals("Doe",customerRegistrationResponse.getLastName());
        assertEquals("doejohn10@gmail.com",customerRegistrationResponse.getEmail());

        verify(passwordEncoder).encode(createCustomerCredentialRequest.getPassword());
        verify(accountOwnerRepository).save(any(AccountOwner.class));
        verify(customerCredentialRepository).save(any(CustomerCredential.class));

    }

    @Test
    void shouldSaveCorrectAccountOwner(){

        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setBillingLanguage(BillingLanguage.ENGLISH);

        CreateCustomerRegistrationRequest  createCustomerRegistrationRequest = new CreateCustomerRegistrationRequest();
        createCustomerRegistrationRequest.setFirstName("John");
        createCustomerRegistrationRequest.setLastName("Doe");
        createCustomerRegistrationRequest.setEmail("doejohn10@gmail.com");
        createCustomerRegistrationRequest.setDateOfBirth(LocalDate.of(1980, 1, 1));

        CreateCustomerCredentialRequest  createCustomerCredentialRequest = new CreateCustomerCredentialRequest();
        createCustomerCredentialRequest.setPassword("Password123!");
        createCustomerCredentialRequest.setConfirmPassword("Password123!");

        when(accountOwnerRepository.save(any(AccountOwner.class)))
                .thenReturn(accountOwner);

        when(passwordEncoder.encode(createCustomerCredentialRequest.getPassword()))
                .thenReturn("passwordtest");

        when(customerCredentialRepository.save(any(CustomerCredential.class)))
                .thenReturn(customerCredential);

        when(accountService.createAccount(createAccountRequest, accountOwner))
                .thenReturn(accountResponse);

        customerRegistrationService.createCustomerRegistration(createAccountRequest, createCustomerRegistrationRequest,
                createCustomerCredentialRequest);

        ArgumentCaptor<AccountOwner> accountOwnerArgumentCaptor = ArgumentCaptor.forClass(AccountOwner.class);

        verify(accountOwnerRepository).save(accountOwnerArgumentCaptor.capture());

        AccountOwner savedAccountOwner = accountOwnerArgumentCaptor.getValue();

        assertEquals("John",savedAccountOwner.getFirstName());
        assertEquals("Doe",savedAccountOwner.getLastName());
        assertEquals("doejohn10@gmail.com", savedAccountOwner.getEmail());
        assertEquals(LocalDate.of(1980, 1, 1),  savedAccountOwner.getDateOfBirth());
        assertNotNull(savedAccountOwner.getCreatedAt());

    }

    @Test
    void shouldSaveCorrectCustomerCredential(){

        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setBillingLanguage(BillingLanguage.ENGLISH);

        CreateCustomerRegistrationRequest  createCustomerRegistrationRequest = new CreateCustomerRegistrationRequest();
        createCustomerRegistrationRequest.setFirstName("John");
        createCustomerRegistrationRequest.setLastName("Doe");
        createCustomerRegistrationRequest.setEmail("doejohn10@gmail.com");
        createCustomerRegistrationRequest.setDateOfBirth(LocalDate.of(1980, 1, 1));

        CreateCustomerCredentialRequest createCustomerCredentialRequest = new CreateCustomerCredentialRequest();
        createCustomerCredentialRequest.setPassword("Password123!");
        createCustomerCredentialRequest.setConfirmPassword("Password123!");

        when(accountOwnerRepository.save(any(AccountOwner.class)))
                .thenReturn(accountOwner);

        when(passwordEncoder.encode(createCustomerCredentialRequest.getPassword()))
                .thenReturn("passwordtest");

        when(customerCredentialRepository.save(any(CustomerCredential.class)))
                .thenReturn(customerCredential);

        when(accountService.createAccount(createAccountRequest, accountOwner))
                .thenReturn(accountResponse);

        customerRegistrationService.createCustomerRegistration(createAccountRequest, createCustomerRegistrationRequest,
                createCustomerCredentialRequest);

        ArgumentCaptor<CustomerCredential> customerCredentialArgumentCaptor = ArgumentCaptor.forClass(CustomerCredential.class);

        verify(customerCredentialRepository).save(customerCredentialArgumentCaptor.capture());

        CustomerCredential savedCustomerCredential = customerCredentialArgumentCaptor.getValue();

        assertFalse(savedCustomerCredential.getEmailVerified());
        assertEquals(accountOwner,  savedCustomerCredential.getAccountOwner());
        assertEquals("passwordtest", savedCustomerCredential.getPasswordHash());
    }
}
