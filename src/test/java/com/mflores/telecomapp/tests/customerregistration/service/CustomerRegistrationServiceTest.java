package com.mflores.telecomapp.tests.customerregistration.service;

import com.mflores.telecomapp.dto.*;
import com.mflores.telecomapp.model.AccountOwner;
import com.mflores.telecomapp.model.BillingLanguage;
import com.mflores.telecomapp.model.CustomerCredential;
import com.mflores.telecomapp.model.EmailVerificationToken;
import com.mflores.telecomapp.repository.AccountOwnerRepository;
import com.mflores.telecomapp.repository.CustomerCredentialRepository;
import com.mflores.telecomapp.service.AccountService;
import com.mflores.telecomapp.service.CustomerRegistrationService;
import static org.junit.jupiter.api.Assertions.*;

import com.mflores.telecomapp.service.EmailService;
import com.mflores.telecomapp.service.EmailVerificationTokenService;
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
    @Mock
    private EmailVerificationTokenService emailVerificationTokenService;
    @Mock
    private EmailService emailService;

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

        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setToken("Tk123456");
        emailVerificationToken.setCreatedAt(createdAt);
        emailVerificationToken.setExpiresAt(createdAt.plusMinutes(2));
        emailVerificationToken.setUsed(false);
        emailVerificationToken.setCustomerCredential(customerCredential);


        when(accountOwnerRepository.save(any(AccountOwner.class)))
                .thenReturn(accountOwner);

        when(passwordEncoder.encode(createCustomerCredentialRequest.getPassword()))
                .thenReturn("passwordtest");

        when(customerCredentialRepository.save(any(CustomerCredential.class)))
                .thenReturn(customerCredential);

        when(emailVerificationTokenService.createVerificationToken(any(CustomerCredential.class)))
                .thenReturn(emailVerificationToken);

        when(accountService.createAccount(createAccountRequest, accountOwner))
                .thenReturn(accountResponse);

        CustomerRegistrationResponse customerRegistrationResponse = customerRegistrationService.createCustomerRegistration(createAccountRequest, createCustomerRegistrationRequest,
                createCustomerCredentialRequest);

        ArgumentCaptor<CustomerCredential> captor = ArgumentCaptor.forClass(CustomerCredential.class);

        assertEquals("TB123456",customerRegistrationResponse.getAccountNumber());
        assertEquals("John",customerRegistrationResponse.getFirstName());
        assertEquals("Doe",customerRegistrationResponse.getLastName());
        assertEquals("doejohn10@gmail.com",customerRegistrationResponse.getEmail());

        verify(customerCredentialRepository).save(captor.capture());
        CustomerCredential customerCredential = captor.getValue();

        assertFalse(customerCredential.getEmailVerified());
        assertEquals(accountOwner,customerCredential.getAccountOwner());
        assertEquals("passwordtest", customerCredential.getPasswordHash());

        verify(passwordEncoder).encode(createCustomerCredentialRequest.getPassword());
        verify(accountOwnerRepository).save(any(AccountOwner.class));
        verify(emailVerificationTokenService).createVerificationToken(customerCredential);
        verify(emailService).sendVerificationEmail(any(String.class), any(String.class));
    }

    @Test
    void shouldSendVerificationEmailAfterSuccessfulRegistration(){

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

        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setToken("Tk123456");
        emailVerificationToken.setCreatedAt(createdAt);
        emailVerificationToken.setExpiresAt(createdAt.plusMinutes(2));
        emailVerificationToken.setUsed(false);
        emailVerificationToken.setCustomerCredential(customerCredential);


        when(accountOwnerRepository.save(any(AccountOwner.class)))
                .thenReturn(accountOwner);

        when(passwordEncoder.encode(createCustomerCredentialRequest.getPassword()))
                .thenReturn("passwordtest");

        when(customerCredentialRepository.save(any(CustomerCredential.class)))
                .thenReturn(customerCredential);

        when(emailVerificationTokenService.createVerificationToken(any(CustomerCredential.class)))
                .thenReturn(emailVerificationToken);

        when(emailService.getVerificationBaseUrl())
                .thenReturn("http://localhost:5173");

        when(accountService.createAccount(createAccountRequest, accountOwner))
                .thenReturn(accountResponse);

        CustomerRegistrationResponse customerRegistrationResponse = customerRegistrationService.createCustomerRegistration(createAccountRequest, createCustomerRegistrationRequest,
                createCustomerCredentialRequest);

        ArgumentCaptor<String> recipientCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> verificationLinkCaptor = ArgumentCaptor.forClass(String.class);

        assertEquals("TB123456",customerRegistrationResponse.getAccountNumber());
        assertEquals("John",customerRegistrationResponse.getFirstName());
        assertEquals("Doe",customerRegistrationResponse.getLastName());
        assertEquals("doejohn10@gmail.com",customerRegistrationResponse.getEmail());

        verify(emailService).sendVerificationEmail(recipientCaptor.capture(), verificationLinkCaptor.capture());

        assertEquals(accountOwner.getEmail(),recipientCaptor.getValue());
        assertEquals(
                "http://localhost:5173/register/verify-email?token=Tk123456",
                verificationLinkCaptor.getValue()
        );

        verify(passwordEncoder).encode(createCustomerCredentialRequest.getPassword());
        verify(accountOwnerRepository).save(any(AccountOwner.class));
        verify(emailVerificationTokenService).createVerificationToken(customerCredential);

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

        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setToken("Tk123456");
        emailVerificationToken.setCreatedAt(createdAt);
        emailVerificationToken.setExpiresAt(createdAt.plusMinutes(2));
        emailVerificationToken.setUsed(false);
        emailVerificationToken.setCustomerCredential(customerCredential);


        when(accountOwnerRepository.save(any(AccountOwner.class)))
                .thenReturn(accountOwner);

        when(passwordEncoder.encode(createCustomerCredentialRequest.getPassword()))
                .thenReturn("passwordtest");

        when(customerCredentialRepository.save(any(CustomerCredential.class)))
                .thenReturn(customerCredential);

        when(emailVerificationTokenService.createVerificationToken(any(CustomerCredential.class)))
                .thenReturn(emailVerificationToken);

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

        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setToken("Tk123456");
        emailVerificationToken.setCreatedAt(createdAt);
        emailVerificationToken.setExpiresAt(createdAt.plusMinutes(2));
        emailVerificationToken.setUsed(false);
        emailVerificationToken.setCustomerCredential(customerCredential);


        when(accountOwnerRepository.save(any(AccountOwner.class)))
                .thenReturn(accountOwner);

        when(passwordEncoder.encode(createCustomerCredentialRequest.getPassword()))
                .thenReturn("passwordtest");

        when(customerCredentialRepository.save(any(CustomerCredential.class)))
                .thenReturn(customerCredential);

        when(emailVerificationTokenService.createVerificationToken(any(CustomerCredential.class)))
                .thenReturn(emailVerificationToken);

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
