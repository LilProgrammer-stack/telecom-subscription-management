package com.mflores.telecomapp.tests.account.service;

import com.mflores.telecomapp.dto.AccountResponse;
import com.mflores.telecomapp.dto.CreateAccountRequest;
import com.mflores.telecomapp.model.Account;
import com.mflores.telecomapp.model.AccountOwner;
import com.mflores.telecomapp.model.AccountStatus;
import com.mflores.telecomapp.model.BillingLanguage;
import com.mflores.telecomapp.repository.AccountRepository;
import com.mflores.telecomapp.service.AccountNumberGenerator;
import com.mflores.telecomapp.service.AccountService;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountNumberGenerator accountNumberGenerator;

    @InjectMocks
    private AccountService accountService;

    @Test
    void shouldCreateAccountWithExpectedProperties() {

        //Arrange
        AccountOwner accountOwner = new AccountOwner(1L, "Miguel", "Flores",
                "floresjosex50@gmail.com", LocalDate.of(2002, 11, 12),
                OffsetDateTime.of(
                        2026, 8, 31,
                        12, 30, 0, 0,
                        ZoneOffset.of("-06:00")
                ));

        CreateAccountRequest accountRequest = new CreateAccountRequest(BillingLanguage.ENGLISH);

        Account account = new Account("12345test", accountRequest.getBillingLanguage(),
                AccountStatus.ACTIVE, OffsetDateTime.of(
                2026, 8, 31,
                12, 30, 0, 0,
                ZoneOffset.of("-06:00")
        ), accountOwner);

        when(accountRepository.save(any(Account.class)))
                .thenReturn(account);

        when(accountNumberGenerator.generate())
                .thenReturn("12345test");

        AccountResponse accountResponse = accountService.createAccount(accountRequest, accountOwner);

        ArgumentCaptor<Account> argumentCaptor = ArgumentCaptor.forClass(Account.class);

        assertEquals(AccountStatus.ACTIVE, accountResponse.getAccountStatus());

        verify(accountRepository).save(argumentCaptor.capture());
        verify(accountNumberGenerator).generate();

        Account capturedAccount = argumentCaptor.getValue();

        assertEquals("12345test", capturedAccount.getAccountNumber());
        assertEquals(BillingLanguage.ENGLISH, capturedAccount.getBillingLanguage());
        assertEquals(AccountStatus.ACTIVE, capturedAccount.getAccountStatus());
        assertEquals(accountOwner,capturedAccount.getAccountOwner());
    }

}
