package com.mflores.telecomapp.service;

import com.mflores.telecomapp.dto.*;
import com.mflores.telecomapp.exception.BusinessRuleViolationException;
import com.mflores.telecomapp.exception.ResourceNotFoundException;
import com.mflores.telecomapp.model.*;
import com.mflores.telecomapp.repository.RoleRepository;
import com.mflores.telecomapp.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerRegistrationService {

    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final EmailService emailService;
    //private final TokenHashingService tokenHashingService;
    //private final SecureTokenGenerator secureTokenGenerator;
    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;
    private final AccountOwnerService accountOwnerService;

    public CustomerRegistrationService(AccountService accountService,
                                       PasswordEncoder passwordEncoder,
                                       EmailVerificationTokenService emailVerificationTokenService,
                                       EmailService emailService,
                                       UserAccountRepository userAccountRepository,
                                       RoleRepository roleRepository,
                                       AccountOwnerService accountOwnerService) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
        this.emailVerificationTokenService = emailVerificationTokenService;
        this.emailService = emailService;
        this.userAccountRepository = userAccountRepository;
        this.roleRepository = roleRepository;
        this.accountOwnerService = accountOwnerService;
    }

    @Transactional
    public CustomerRegistrationResponse createCustomerRegistration(CreateAccountRequest createAccountRequest,
                                                                   CreateCustomerRegistrationRequest createCustomerRegistrationRequest,
                                                                   CreateCustomerCredentialRequest createCustomerCredentialRequest) {

        if (userAccountRepository.existsByEmail(
                createCustomerRegistrationRequest.getEmail())) {
            throw new BusinessRuleViolationException("Email already exists");
        }
        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(createCustomerRegistrationRequest.getEmail());
        userAccount.setEmailVerified(false);
        Role role = roleRepository.findByRoleName("ROLE_CUSTOMER")
                .orElseThrow(()->new ResourceNotFoundException("Role not found"));
        userAccount.setRole(role);
        String hashedPassword = passwordEncoder.encode(createCustomerCredentialRequest.getPassword());
        userAccount.setPasswordHash(hashedPassword);
        UserAccount savedUserAccount = userAccountRepository.save(userAccount);

        AccountOwner savedAccountOwner = accountOwnerService
                .createAccountOwner(createCustomerRegistrationRequest, savedUserAccount);

        //customerCredential.setRegistrationEditTokenExpiresAt(OffsetDateTime.now().plusMinutes(15));

        //String registrationEditToken = secureTokenGenerator.generateToken();
        //String registrationEditTokenHash = tokenHashingService.hashToken(registrationEditToken);

        //customerCredential.setRegistrationEditTokenHash(registrationEditTokenHash);
        //CustomerCredential savedCredential = customerCredentialRepository.save(customerCredential);
        AccountResponse savedAccountResponse = accountService.createAccount(createAccountRequest, savedAccountOwner);

        EmailVerificationToken returnedEmailToken = emailVerificationTokenService.createEmailVerificationToken(savedUserAccount);

        String verificationLink =
                emailService.getVerificationBaseUrl()
                        + "/register/verify-email?token="
                        + returnedEmailToken.getToken();

        emailService.sendVerificationEmail(savedAccountOwner.getUserAccount().getEmail(), verificationLink);

        return new CustomerRegistrationResponse(savedAccountOwner.getFirstName(),
                savedAccountOwner.getLastName(), savedAccountOwner.getUserAccount().getEmail(),
                savedAccountResponse.getAccountNumber());
    }
}
