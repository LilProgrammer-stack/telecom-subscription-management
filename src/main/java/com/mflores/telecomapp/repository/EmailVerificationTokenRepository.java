package com.mflores.telecomapp.repository;

import com.mflores.telecomapp.model.CustomerCredential;
import com.mflores.telecomapp.model.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByCustomerCredential(CustomerCredential customerCredential);
    Optional<EmailVerificationToken> findByToken(String token);
}
