package com.mflores.telecomapp.service;

import com.mflores.telecomapp.exception.BusinessRuleViolationException;
import com.mflores.telecomapp.model.CustomerCredential;
import com.mflores.telecomapp.model.EmailVerificationToken;
import com.mflores.telecomapp.model.UserAccount;
import com.mflores.telecomapp.repository.EmailVerificationTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class EmailVerificationTokenService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final SecureTokenGenerator secureTokenGenerator;

    public EmailVerificationTokenService(EmailVerificationTokenRepository emailVerificationTokenRepository,
                                         SecureTokenGenerator secureTokenGenerator) {
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.secureTokenGenerator = secureTokenGenerator;
    }

    @Transactional
    public EmailVerificationToken createEmailVerificationToken(UserAccount userAccount) {

        Optional<EmailVerificationToken> foundToken = emailVerificationTokenRepository.findByUserAccount(userAccount);
        OffsetDateTime now = OffsetDateTime.now();

        if (foundToken.isPresent()) {
            EmailVerificationToken presentToken = foundToken.get();
            if (presentToken.getCreatedAt().plusSeconds(30).isAfter(now)) {
                throw new BusinessRuleViolationException("A new request can be sent once thirty seconds have passed.");
            }else {
                emailVerificationTokenRepository.delete(presentToken);
            }
        }

        EmailVerificationToken savedEmailVerificationToken = emailVerificationTokenRepository.save(generateToken(userAccount));
        return savedEmailVerificationToken;
    }

    private EmailVerificationToken generateToken(UserAccount userAccount) {
        String generatedToken = secureTokenGenerator.generateToken();
        OffsetDateTime now = OffsetDateTime.now();

        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setToken(generatedToken); //Unique random secret gets generated
        emailVerificationToken.setCreatedAt(now);
        emailVerificationToken.setExpiresAt(now.plusMinutes(2));
        //emailVerificationToken.setUserAccount(userAccount); //we know WHO is requesting verification
        emailVerificationToken.setUsed(false);

        return emailVerificationToken;
    }


    @Transactional
    public void verifyEmailToken(String token) {

        EmailVerificationToken foundToken = emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessRuleViolationException("Token does not exist"));

        if (consumeToken(foundToken)) {
          //  foundToken.getUserAccount().setEmailVerified(true);
        }else {
            throw new BusinessRuleViolationException("Verification token is expired or has already been used.");
        }
    }

    private boolean consumeToken(EmailVerificationToken token) {

        if (token.isUsed()) {
            return false;
        }
        if (token.getExpiresAt().isBefore(OffsetDateTime.now())) {
            return false;
        }

        token.setUsed(true);
        return true;
    }


}
