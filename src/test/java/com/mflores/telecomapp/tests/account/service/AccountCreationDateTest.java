package com.mflores.telecomapp.tests.account.service;

import com.mflores.telecomapp.model.Account;
import com.mflores.telecomapp.model.AccountOwner;
import com.mflores.telecomapp.model.AccountStatus;
import com.mflores.telecomapp.model.BillingLanguage;
import com.mflores.telecomapp.repository.AccountOwnerRepository;
import com.mflores.telecomapp.repository.AccountRepository;
import com.mflores.telecomapp.service.AccountNumberGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AccountNumberGenerator.class)
public class AccountCreationDateTest {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountNumberGenerator accountNumberGenerator;
    @Autowired
    private AccountOwnerRepository accountOwnerRepository;

    @Test
    void shouldReturnCreationDate(){

        AccountOwner accountOwner = new AccountOwner();
        accountOwner.setFirstName("Jose");
        accountOwner.setLastName("Flores");
        accountOwner.setEmail("floresjosex50@gmail.com");
        accountOwner.setDateOfBirth(LocalDate.of(2002,11,14));
        accountOwner.setCreatedAt(OffsetDateTime.of(
                2026, 8, 31,
                12, 30, 0, 0,
                ZoneOffset.of("-06:00")));

        Account account = new Account();
        account.setBillingLanguage(BillingLanguage.ENGLISH);
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setAccountNumber(accountNumberGenerator.generate());

        AccountOwner savedAccountOwner = accountOwnerRepository.saveAndFlush(accountOwner);
        account.setAccountOwner(savedAccountOwner);

        Account savedAccount = accountRepository.saveAndFlush(account);

        Assertions.assertNotNull(savedAccount.getCreationDate());
    }
}
