package com.mflores.telecomapp.tests.emailverificationtoken;
import com.mflores.telecomapp.exception.BusinessRuleViolationException;
import com.mflores.telecomapp.model.AccountOwner;
import com.mflores.telecomapp.model.CustomerCredential;
import com.mflores.telecomapp.model.EmailVerificationToken;
import com.mflores.telecomapp.repository.EmailVerificationTokenRepository;
import com.mflores.telecomapp.service.EmailTokenGenerator;
import com.mflores.telecomapp.service.EmailVerificationTokenService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmailVerificationTokenServiceTest {

    @Mock
    private EmailVerificationTokenRepository emailVerificationTokenRepository;
    @Mock
    private EmailTokenGenerator  emailTokenGenerator;
    @InjectMocks
    private EmailVerificationTokenService emailVerificationTokenService;

    private final CustomerCredential  credential = new CustomerCredential();

    @BeforeEach
    void setUp() {
        credential.setEmailVerified(false);
        credential.setPasswordHash("password");
        AccountOwner accountOwner = new AccountOwner();
        credential.setAccountOwner(accountOwner);
    }

    @Test
    void shouldCreateVerificationTokenSuccessfully() {

        when(emailTokenGenerator.generateEmailToken())
                .thenReturn("token");

        when(emailVerificationTokenRepository.save(any(EmailVerificationToken.class)))
                .thenAnswer(i -> i.getArgument(0));

        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.createVerificationToken(credential);

        assertEquals(credential, emailVerificationToken.getCustomerCredential());
        assertEquals("token", emailVerificationToken.getToken());
        assertFalse(emailVerificationToken.getUsed());
        assertTrue(emailVerificationToken.getExpiresAt().isAfter(emailVerificationToken.getCreatedAt()));

        verify(emailVerificationTokenRepository, never()).delete(any(EmailVerificationToken.class));
        verify(emailVerificationTokenRepository).save(any(EmailVerificationToken.class));
    }

    @Test
    void shouldDeleteOldVerificationTokenSuccessfully() {

        EmailVerificationToken oldToken = new  EmailVerificationToken();
        oldToken.setToken("oldToken");
        oldToken.setUsed(false);
        OffsetDateTime createdAt = OffsetDateTime.of(2025, 11, 12, 0, 0, 0, 0, ZoneOffset.UTC);
        oldToken.setCreatedAt(createdAt);
        oldToken.setExpiresAt(createdAt.plusMinutes(2));
        oldToken.setCustomerCredential(credential);

        when(emailVerificationTokenRepository.findByCustomerCredential(credential))
                .thenReturn(Optional.of(oldToken));

        when(emailTokenGenerator.generateEmailToken())
                .thenReturn("token");

        when(emailVerificationTokenRepository.save(any(EmailVerificationToken.class)))
                .thenAnswer(i -> i.getArgument(0));

        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.createVerificationToken(credential);

        assertNotEquals(oldToken, emailVerificationToken);
        assertNotEquals(oldToken.getToken(), emailVerificationToken.getToken());
        assertEquals("token", emailVerificationToken.getToken());

        verify(emailVerificationTokenRepository).delete(oldToken);
        verify(emailVerificationTokenRepository).save(any(EmailVerificationToken.class));
    }

    @Test
    void shouldRejectEmailVerificationToken() {

        EmailVerificationToken recentToken = new  EmailVerificationToken();
        recentToken.setToken("recentToken");
        recentToken.setUsed(false);
        OffsetDateTime createdAt = OffsetDateTime.now();
        recentToken.setCreatedAt(createdAt);
        recentToken.setExpiresAt(createdAt.plusMinutes(2));
        recentToken.setCustomerCredential(credential);

        when(emailVerificationTokenRepository.findByCustomerCredential(credential))
                .thenReturn(Optional.of(recentToken));

        assertThrows(BusinessRuleViolationException.class, () -> emailVerificationTokenService.createVerificationToken(credential));

        verify(emailVerificationTokenRepository, never()).delete(any(EmailVerificationToken.class));
        verify(emailVerificationTokenRepository, never()).save(any(EmailVerificationToken.class));
        verify(emailTokenGenerator, never()).generateEmailToken();
    }

    @Test
    void shouldVerifyEmailSuccessfullyWithValidToken(){

        OffsetDateTime createdAt = OffsetDateTime.now();

        EmailVerificationToken existingToken = new  EmailVerificationToken();
        existingToken.setCreatedAt(createdAt);
        existingToken.setExpiresAt(createdAt.plusMinutes(2));
        existingToken.setToken("existingToken");
        existingToken.setUsed(false);
        existingToken.setCustomerCredential(credential);

        when(emailVerificationTokenRepository.findByToken("existingToken"))
                .thenReturn(Optional.of(existingToken));

        emailVerificationTokenService.verifyEmailToken("existingToken");

        assertTrue(existingToken.getUsed());
        assertTrue(credential.getEmailVerified());
    }

    @Test
    void shouldNotVerifyEmailTokenWhenItHasExpired(){

        EmailVerificationToken existingToken = new  EmailVerificationToken();
        OffsetDateTime now = OffsetDateTime.of(2025, 11, 12, 0, 0, 0, 0, ZoneOffset.UTC);
        existingToken.setCreatedAt(now);
        existingToken.setExpiresAt(now.plusMinutes(2));
        existingToken.setToken("existingToken");
        existingToken.setUsed(false);
        existingToken.setCustomerCredential(credential);

        when(emailVerificationTokenRepository.findByToken("existingToken"))
                .thenReturn(Optional.of(existingToken));

        assertThrows(BusinessRuleViolationException.class, () -> emailVerificationTokenService.verifyEmailToken("existingToken"));

        assertFalse(existingToken.getUsed());
        assertFalse(credential.getEmailVerified());
    }

    @Test
    void shouldNotVerifyEmailTokenWhenItHasBeenUsed(){

        EmailVerificationToken existingToken = new  EmailVerificationToken();
        OffsetDateTime now = OffsetDateTime.now();
        existingToken.setCreatedAt(now);
        existingToken.setExpiresAt(now.plusMinutes(2));
        existingToken.setToken("existingToken");
        existingToken.setUsed(true);
        existingToken.setCustomerCredential(credential);

        when(emailVerificationTokenRepository.findByToken("existingToken"))
                .thenReturn(Optional.of(existingToken));

        assertThrows(BusinessRuleViolationException.class, () -> emailVerificationTokenService.verifyEmailToken("existingToken"));

        assertTrue(existingToken.getUsed());
        assertFalse(credential.getEmailVerified());
    }

    @Test
    void shouldRejectEmailVerificationWhenTokenDoesNotExist(){

        when(emailVerificationTokenRepository.findByToken("existingToken"))
        .thenReturn(Optional.empty());

        assertThrows(BusinessRuleViolationException.class, () -> emailVerificationTokenService.verifyEmailToken("existingToken"));

        assertFalse(credential.getEmailVerified());

        verify(emailVerificationTokenRepository, never())
                .save(any(EmailVerificationToken.class));

        verify(emailVerificationTokenRepository, never())
                .delete(any(EmailVerificationToken.class));
    }
}
