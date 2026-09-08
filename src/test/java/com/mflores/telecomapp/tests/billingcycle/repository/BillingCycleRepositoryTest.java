package com.mflores.telecomapp.tests.billingcycle.repository;

import com.mflores.telecomapp.model.*;
import com.mflores.telecomapp.repository.AccountOwnerRepository;
import com.mflores.telecomapp.repository.AccountRepository;
import com.mflores.telecomapp.repository.BillingCycleRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BillingCycleRepositoryTest {

    @Autowired
    private BillingCycleRepository billingCycleRepository;
    @Autowired
    private AccountOwnerRepository accountOwnerRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldCreateBillingCycleSuccessfully() {

        AccountOwner accountOwner = new AccountOwner("Miguel", "Flores", "floresjosex50@gmail.com",
                LocalDate.of(2002, 11, 12));

        accountOwnerRepository.save(accountOwner);

        Account account = new Account("123", BillingLanguage.ENGLISH, AccountStatus.ACTIVE, OffsetDateTime.now(),
                accountOwner);

        accountRepository.save(account);

        BillingCycle billingCycle = new BillingCycle(1,
                LocalDate.of(2002, 12, 13), LocalDate.of(2002, 12, 12),
                account);

        billingCycleRepository.saveAndFlush(billingCycle);

        assertNotNull(billingCycle.getBillingCycleId());
    }

    /*
    ✓ shouldCreateBillingCycleSuccessfully
    ✓ shouldNotAllowNullAccount
    ✓ shouldNotAllowNullPeriodStartDate
    ✓ shouldNotAllowNullPeriodEndDate
    ✓ shouldNotAllowNullDueDate
    ✓ shouldNotAllowDuplicateCycleNumberForSameAccount
     */

    @Test
    void shouldNotAllowNullAccount() {

        BillingCycle billingCycle = new BillingCycle(1,
                LocalDate.of(2002, 12, 13), LocalDate.of(2002, 12, 12),
                null);

        assertThrows(DataIntegrityViolationException.class, () -> billingCycleRepository.saveAndFlush(billingCycle));
    }

    @Test
    void shouldNotAllowNullPeriodStartDate(){

        AccountOwner accountOwner = new AccountOwner("Miguel", "Flores", "floresjosex50@gmail.com",
                LocalDate.of(2002, 11, 12));

        accountOwnerRepository.save(accountOwner);

        Account account = new Account("123", BillingLanguage.ENGLISH, AccountStatus.ACTIVE, OffsetDateTime.now(),
                accountOwner);

        accountRepository.save(account);

        BillingCycle billingCycle = new BillingCycle(1, null,
                LocalDate.of(2002, 12, 13),
                account);

        assertThrows(DataIntegrityViolationException.class, () -> billingCycleRepository.saveAndFlush(billingCycle));
    }

    @Test
    void shouldNotAllowNullPeriodEndDate(){

        AccountOwner accountOwner = new AccountOwner("Miguel", "Flores", "floresjosex50@gmail.com",
                LocalDate.of(2002, 11, 12));

        accountOwnerRepository.save(accountOwner);

        Account account = new Account("123", BillingLanguage.ENGLISH, AccountStatus.ACTIVE, OffsetDateTime.now(),
                accountOwner);

        accountRepository.save(account);

        BillingCycle billingCycle = new BillingCycle(1,
                null, LocalDate.of(2002, 12, 12),
                account);

        assertThrows(DataIntegrityViolationException.class, () -> billingCycleRepository.saveAndFlush(billingCycle));
    }

    @Test
    void shouldNotAllowNullDueDate(){

        AccountOwner accountOwner = new AccountOwner("Miguel", "Flores", "floresjosex50@gmail.com",
                LocalDate.of(2002, 11, 12));

        accountOwnerRepository.save(accountOwner);

        Account account = new Account("123", BillingLanguage.ENGLISH, AccountStatus.ACTIVE, OffsetDateTime.now(),
                accountOwner);

        accountRepository.save(account);

        BillingCycle billingCycle = new BillingCycle(1,
                LocalDate.of(2002, 12, 13), null,
                account);

        assertThrows(DataIntegrityViolationException.class, () -> billingCycleRepository.saveAndFlush(billingCycle));
    }

    @Test
    void shouldNotAllowDuplicateCycleNumberForSameAccount(){
        AccountOwner accountOwner = new AccountOwner("Miguel", "Flores", "floresjosex50@gmail.com",
                LocalDate.of(2002, 11, 12));

        accountOwnerRepository.save(accountOwner);

        Account account = new Account("123", BillingLanguage.ENGLISH, AccountStatus.ACTIVE, OffsetDateTime.now(),
                accountOwner);

        accountRepository.save(account);

        BillingCycle billingCycle1 = new BillingCycle(2,
                LocalDate.of(2002, 12, 13), LocalDate.of(2002, 12, 12),
                account);

        billingCycleRepository.save(billingCycle1);

        BillingCycle billingCycle2 = new BillingCycle(2,
                LocalDate.of(2003, 01, 13), LocalDate.of(2002, 12, 12),
                account);

        List<BillingCycle> billingCycleList = new ArrayList<>();
        account.setBillingCycles(billingCycleList);

        assertThrows(DataIntegrityViolationException.class, () -> billingCycleRepository.saveAndFlush(billingCycle2));
    }

    @Test
    void shouldReturnTheMostRecentBillingCycleBasedOnAccount(){

        AccountOwner accountOwner = new AccountOwner("Miguel", "Flores", "floresjosex50@gmail.com",
                LocalDate.of(2002, 11, 12));
        accountOwnerRepository.save(accountOwner);

        Account account = new Account("123", BillingLanguage.ENGLISH, AccountStatus.ACTIVE, OffsetDateTime.now(),
                accountOwner);
        accountRepository.save(account);

        BillingCycle billingCycle1 = new BillingCycle(1, LocalDate.of(2026,5,11),
                LocalDate.of(2026,6,10), account);
        billingCycleRepository.saveAndFlush(billingCycle1);
        BillingCycle billingCycle2 = new BillingCycle(2, LocalDate.of(2026,6,11),
                LocalDate.of(2026,7,10), account);
        billingCycleRepository.saveAndFlush(billingCycle2);
        BillingCycle billingCycle3 = new BillingCycle(3, LocalDate.of(2026,7,11),
                LocalDate.of(2026,8,10), account);
        billingCycleRepository.saveAndFlush(billingCycle3);

        BillingCycle billingCycle = billingCycleRepository.findFirstByAccountOrderByCycleNumberDesc(account).get();

        assertEquals(3, billingCycle.getCycleNumber());


    }
}
