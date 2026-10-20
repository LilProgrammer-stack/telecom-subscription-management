package com.mflores.telecomapp.service;

import com.mflores.telecomapp.exception.BusinessRuleViolationException;
import com.mflores.telecomapp.model.CustomerCredential;
import com.mflores.telecomapp.model.EmailVerificationToken;
import com.mflores.telecomapp.repository.EmailVerificationTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class EmailVerificationTokenService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final EmailTokenGenerator emailTokenGenerator;

    public EmailVerificationTokenService(EmailVerificationTokenRepository emailVerificationTokenRepository,
                                         EmailTokenGenerator emailTokenGenerator) {
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.emailTokenGenerator = emailTokenGenerator;
    }

    @Transactional
    public EmailVerificationToken createVerificationToken(CustomerCredential customerCredential) {

        Optional<EmailVerificationToken> foundToken = emailVerificationTokenRepository.findByCustomerCredential(customerCredential);
        OffsetDateTime now = OffsetDateTime.now();

        if (foundToken.isPresent()) {
            EmailVerificationToken presentToken = foundToken.get();
            if (presentToken.getCreatedAt().plusSeconds(30).isAfter(now)) {
                throw new BusinessRuleViolationException("A new request can be sent once thirty seconds have passed.");
            }else {
                emailVerificationTokenRepository.delete(presentToken);
            }
        }

        EmailVerificationToken savedEmailVerificationToken = emailVerificationTokenRepository.save(generateEmailToken(customerCredential));
        return savedEmailVerificationToken;
    }

    private EmailVerificationToken generateEmailToken(CustomerCredential customerCredential) {
        String generatedToken = emailTokenGenerator.generateEmailToken();
        OffsetDateTime now = OffsetDateTime.now();

        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setToken(generatedToken); //Unique random secret gets generated
        emailVerificationToken.setCreatedAt(now);
        emailVerificationToken.setExpiresAt(now.plusMinutes(2));
        emailVerificationToken.setCustomerCredential(customerCredential); //we know WHO is requesting verification
        emailVerificationToken.setUsed(false);

        return emailVerificationToken;
    }


    @Transactional
    public void verifyEmailToken(String token) {

        EmailVerificationToken foundToken = emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessRuleViolationException("Token does not exist"));

        if (consumeToken(foundToken)) {
            foundToken.getCustomerCredential().setEmailVerified(true);
        }else {
            throw new BusinessRuleViolationException("Verification token is expired or has already been used.");
        }
    }

    private boolean consumeToken(EmailVerificationToken token) {

        if (token.getUsed()){
            return false;
        }
        if (token.getExpiresAt().isBefore(OffsetDateTime.now())) {
            return false;
        }

        token.setUsed(true);
        return true;
    }


}
