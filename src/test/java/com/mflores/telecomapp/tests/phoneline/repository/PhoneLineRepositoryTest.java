package com.mflores.telecomapp.tests.phoneline.repository;

import com.mflores.telecomapp.exception.ResourceNotFoundException;
import com.mflores.telecomapp.model.*;
import com.mflores.telecomapp.repository.AccountOwnerRepository;
import com.mflores.telecomapp.repository.AccountRepository;
import com.mflores.telecomapp.repository.PhoneLineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PhoneLineRepositoryTest {

    @Autowired
    private AccountOwnerRepository accountOwnerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PhoneLineRepository phoneLineRepository;

    private AccountOwner accountOwner = new AccountOwner();
    private Account account  = new Account();

    @BeforeEach
    void setup() {
        accountOwner.setFirstName("John");
        accountOwner.setLastName("Smith");
        accountOwner.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        accountOwner.setEmail("smithjohn1@gmail.com");
        accountOwner.setDateOfBirth(LocalDate.of(2001, 04, 04));

        accountOwnerRepository.save(accountOwner);

        account.setAccountNumber("12346test");
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setBillingLanguage(BillingLanguage.ENGLISH);
        account.setCreationDate(OffsetDateTime.now(ZoneOffset.UTC));
        account.setAccountOwner(accountOwner);

        accountRepository.save(account);
    }

    @Test
    void shouldCreatePhoneLineSuccessfully() {


        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setPhoneNumber("6666666666");
        phoneLine.setPhoneLineStatus(PhoneLineStatus.ACTIVE);
        phoneLine.setAccount(account);

        PhoneLine savedPhoneLine = phoneLineRepository.saveAndFlush(phoneLine);
        assertNotNull(savedPhoneLine);

        PhoneLine foundPhoneLine = phoneLineRepository.findById(savedPhoneLine.getPhoneLineId())
                .orElseThrow(()-> new ResourceNotFoundException("Phone line "+savedPhoneLine.getPhoneNumber()+" not found"));

        assertEquals("6666666666",  foundPhoneLine.getPhoneNumber());
        assertEquals(PhoneLineStatus.ACTIVE, foundPhoneLine.getPhoneLineStatus());
        assertEquals("12346test", foundPhoneLine.getAccount().getAccountNumber());
        assertEquals("John", foundPhoneLine.getAccount().getAccountOwner().getFirstName());
        assertEquals("Smith", foundPhoneLine.getAccount().getAccountOwner().getLastName());
    }

    @Test
    void shouldNotAllowDuplicatePhoneNumberOnTheSameAccount() {
        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setPhoneNumber("6666666666");
        phoneLine.setPhoneLineStatus(PhoneLineStatus.ACTIVE);
        phoneLine.setAccount(account);

        phoneLineRepository.saveAndFlush(phoneLine);

        PhoneLine phoneLine2 = new PhoneLine();
        phoneLine2.setPhoneNumber("6666666666");
        phoneLine2.setPhoneLineStatus(PhoneLineStatus.ACTIVE);
        phoneLine2.setAccount(account);

        assertThrows(DataIntegrityViolationException.class, () -> phoneLineRepository.saveAndFlush(phoneLine2));
    }

    @Test
    void shouldNotAllowDuplicatePhoneNumberOnDifferentAccounts() {

        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setPhoneNumber("6666666666");
        phoneLine.setPhoneLineStatus(PhoneLineStatus.ACTIVE);
        phoneLine.setAccount(account);

        phoneLineRepository.saveAndFlush(phoneLine);

        AccountOwner accountOwner2 = new AccountOwner();
        accountOwner2.setFirstName("Maria");
        accountOwner2.setLastName("Flores");
        accountOwner2.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        accountOwner2.setEmail("floresmaria1@gmail.com");
        accountOwner2.setDateOfBirth(LocalDate.of(2001, 4, 4));
        accountOwnerRepository.save(accountOwner2);

        Account account2 = new Account();
        account2.setAccountNumber("12344test");
        account2.setAccountStatus(AccountStatus.ACTIVE);
        account2.setBillingLanguage(BillingLanguage.ENGLISH);
        account2.setCreationDate(OffsetDateTime.now(ZoneOffset.UTC));
        account2.setAccountOwner(accountOwner2);
        accountRepository.save(account2);

        PhoneLine phoneLine2 = new PhoneLine();
        phoneLine2.setPhoneNumber("6666666666");
        phoneLine2.setPhoneLineStatus(PhoneLineStatus.ACTIVE);
        phoneLine2.setAccount(account2);

        assertThrows(DataIntegrityViolationException.class, () -> phoneLineRepository.saveAndFlush(phoneLine2));
    }

    @Test
    void shouldNotAllowPhoneLineWithoutStatus() {
        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setPhoneNumber("6666666666");
        phoneLine.setPhoneLineStatus(null);
        phoneLine.setAccount(account);

        assertThrows(DataIntegrityViolationException.class, () -> phoneLineRepository.saveAndFlush(phoneLine));
    }

    @Test
    void shouldNotAllowPhoneLineWithoutAccount() {
        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setPhoneNumber("6666666666");
        phoneLine.setPhoneLineStatus(PhoneLineStatus.ACTIVE);
        phoneLine.setAccount(null);

        assertThrows(DataIntegrityViolationException.class, () -> phoneLineRepository.saveAndFlush(phoneLine));
    }

    @Test
    void shouldNotAllowPhoneLineWithoutPhoneNumber() {
        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setPhoneNumber(null);
        phoneLine.setPhoneLineStatus(PhoneLineStatus.ACTIVE);
        phoneLine.setAccount(account);

        assertThrows(DataIntegrityViolationException.class, () -> phoneLineRepository.saveAndFlush(phoneLine));
    }

    @Test
    void shouldNotAllowPhoneNumberWithInvalidFormat(){
        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setPhoneNumber("66-666666#");
        phoneLine.setPhoneLineStatus(PhoneLineStatus.ACTIVE);
        phoneLine.setAccount(account);

        assertThrows(DataIntegrityViolationException.class, () -> phoneLineRepository.saveAndFlush(phoneLine));
    }
}
