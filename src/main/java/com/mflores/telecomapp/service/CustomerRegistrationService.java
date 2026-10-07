package com.mflores.telecomapp.service;

import com.mflores.telecomapp.dto.*;
import com.mflores.telecomapp.exception.BusinessRuleViolationException;
import com.mflores.telecomapp.model.AccountOwner;
import com.mflores.telecomapp.model.CustomerCredential;
import com.mflores.telecomapp.repository.AccountOwnerRepository;
import com.mflores.telecomapp.repository.CustomerCredentialRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class CustomerRegistrationService {

    private final AccountOwnerRepository accountOwnerRepository;
    private final AccountService accountService;
    private final CustomerCredentialRepository customerCredentialRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerRegistrationService(AccountOwnerRepository accountOwnerRepository, AccountService accountService, CustomerCredentialRepository customerCredentialRepository, PasswordEncoder passwordEncoder) {
        this.accountOwnerRepository = accountOwnerRepository;
        this.accountService = accountService;
        this.customerCredentialRepository = customerCredentialRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public CustomerRegistrationResponse createCustomerRegistration(CreateAccountRequest createAccountRequest,
                                                                   CreateCustomerRegistrationRequest createCustomerRegistrationRequest,
                                                                   CreateCustomerCredentialRequest createCustomerCredentialRequest) {

        AccountOwner accountOwner = new AccountOwner();
        accountOwner.setFirstName(createCustomerRegistrationRequest.getFirstName());
        accountOwner.setLastName(createCustomerRegistrationRequest.getLastName());
        accountOwner.setDateOfBirth(createCustomerRegistrationRequest.getDateOfBirth());
        accountOwner.setEmail(createCustomerRegistrationRequest.getEmail());
        accountOwner.setCreatedAt(OffsetDateTime.now());
        AccountOwner savedAccountOwner = accountOwnerRepository.save(accountOwner);

        String hashedPassword = passwordEncoder.encode(createCustomerCredentialRequest.getPassword());;

        CustomerCredential customerCredential = new CustomerCredential();
        customerCredential.setAccountOwner(savedAccountOwner);
        customerCredential.setEmailVerified(false);
        customerCredential.setPasswordHash(hashedPassword);
        customerCredentialRepository.save(customerCredential);

        AccountResponse savedAccountResponse = accountService.createAccount(createAccountRequest, savedAccountOwner);

        return new CustomerRegistrationResponse(savedAccountOwner.getFirstName(),
                savedAccountOwner.getLastName(), savedAccountOwner.getEmail(),
                savedAccountResponse.getAccountNumber());
    }
}
