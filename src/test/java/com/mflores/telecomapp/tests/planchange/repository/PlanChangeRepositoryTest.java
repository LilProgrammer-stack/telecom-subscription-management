package com.mflores.telecomapp.tests.planchange.repository;

import com.mflores.telecomapp.model.*;
import com.mflores.telecomapp.repository.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Currency;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlanChangeRepositoryTest {

    @Autowired
    private PlanChangeRepository planChangeRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private PhoneLineRepository phoneLineRepository;

    @Autowired
    private AccountOwnerRepository accountOwnerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BillingCycleRepository billingCycleRepository;

    private AccountOwner accountOwner = new AccountOwner();
    private Account account = new Account();
    private BillingCycle  billingCycle = new BillingCycle();

    private Plan currentPlan = new Plan();
    private Plan requestedPlan = new Plan();
    private PhoneLine phoneLine = new PhoneLine();

    @BeforeEach
    public void setup() {

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

        billingCycle.setCycleNumber(2);
        billingCycle.setPeriodStartDate(LocalDate.of(2026, 04, 9));
        billingCycle.setPeriodEndDate(LocalDate.of(2026, 05, 8));
        billingCycle.setAccount(account);

        billingCycleRepository.save(billingCycle);

        currentPlan.setPlanName("Advanced");
        Money money = new Money(10000, Currency.getInstance("USD"));
        currentPlan.setPrice(money);
        currentPlan.setDataLimitMb(100L);
        currentPlan.setRoamingLimitMb(100L);
        currentPlan.setHotspotLimitMb(100L);
        currentPlan.setDescription("Advanced Plan");
        currentPlan.setActive(true);

        planRepository.save(currentPlan);


        requestedPlan.setPlanName("Basic");
        Money money2 = new Money(10000, Currency.getInstance("USD"));
        requestedPlan.setPrice(money2);
        requestedPlan.setDataLimitMb(100L);
        requestedPlan.setRoamingLimitMb(100L);
        requestedPlan.setHotspotLimitMb(100L);
        requestedPlan.setDescription("Basic Plan");
        requestedPlan.setActive(true);

        planRepository.save(requestedPlan);


        phoneLine.setPhoneNumber("5555555555");
        phoneLine.setPhoneLineStatus(PhoneLineStatus.ACTIVE);
        phoneLine.setAccount(account);

        phoneLineRepository.save(phoneLine);
    }

    @Test
    void shouldCreatePlanChangeSuccessfully() {
        PlanChange planChange = new PlanChange();
        planChange.setCurrentPlan(currentPlan);
        planChange.setRequestedPlan(requestedPlan);
        planChange.setStatus(PlanChangeStatus.PENDING);
        planChange.setRequestedAt(OffsetDateTime.now(ZoneOffset.UTC));
        planChange.setEffectiveAt(billingCycle.getPeriodEndDate().atTime(0,0,0)
                .atOffset(ZoneOffset.UTC));
        planChange.setPhoneLine(phoneLine);

        PlanChange savedPlanChange = planChangeRepository.saveAndFlush(planChange);

        assertEquals(currentPlan, savedPlanChange.getCurrentPlan());
        assertEquals(requestedPlan, savedPlanChange.getRequestedPlan());
        assertEquals(PlanChangeStatus.PENDING, savedPlanChange.getStatus());
        assertEquals(phoneLine, savedPlanChange.getPhoneLine());
    }

    @Test
    void shouldThrowExceptionWhenCurrentPlanIsNull() {
        PlanChange planChange = new PlanChange();
        planChange.setCurrentPlan(null);
        planChange.setRequestedPlan(requestedPlan);
        planChange.setStatus(PlanChangeStatus.PENDING);
        planChange.setRequestedAt(OffsetDateTime.now(ZoneOffset.UTC));
        planChange.setEffectiveAt(billingCycle.getPeriodEndDate().atTime(0,0,0)
                .atOffset(ZoneOffset.UTC));
        planChange.setPhoneLine(phoneLine);

        assertThrows(DataIntegrityViolationException.class, () -> planChangeRepository.saveAndFlush(planChange));
    }

    @Test
    void shouldThrowExceptionWhenRequestedPlanIsNull() {
        PlanChange planChange = new PlanChange();
        planChange.setCurrentPlan(currentPlan);
        planChange.setRequestedPlan(null);
        planChange.setStatus(PlanChangeStatus.PENDING);
        planChange.setRequestedAt(OffsetDateTime.now(ZoneOffset.UTC));
        planChange.setEffectiveAt(billingCycle.getPeriodEndDate().atTime(0,0,0)
                .atOffset(ZoneOffset.UTC));
        planChange.setPhoneLine(phoneLine);

        assertThrows(DataIntegrityViolationException.class, () -> planChangeRepository.saveAndFlush(planChange));
    }

    @Test
    void shouldThrowExceptionWhenRequestedAtIsNull() {
        PlanChange planChange = new PlanChange();
        planChange.setCurrentPlan(currentPlan);
        planChange.setRequestedPlan(requestedPlan);
        planChange.setStatus(PlanChangeStatus.PENDING);
        planChange.setRequestedAt(null);
        planChange.setEffectiveAt(billingCycle.getPeriodEndDate().atTime(0,0,0)
                .atOffset(ZoneOffset.UTC));
        planChange.setPhoneLine(phoneLine);

        assertThrows(DataIntegrityViolationException.class, () -> planChangeRepository.saveAndFlush(planChange));
    }

    @Test
    void shouldThrowExceptionWhenEffectiveAtIsNull() {
        PlanChange planChange = new PlanChange();
        planChange.setCurrentPlan(currentPlan);;
        planChange.setRequestedPlan(requestedPlan);
        planChange.setStatus(PlanChangeStatus.PENDING);
        planChange.setRequestedAt(OffsetDateTime.now(ZoneOffset.UTC));
        planChange.setEffectiveAt(null);
        planChange.setPhoneLine(phoneLine);

        assertThrows(DataIntegrityViolationException.class, () -> planChangeRepository.saveAndFlush(planChange));
    }

    @Test
    void shouldThrowExceptionWhenStatusIsNull() {
        PlanChange planChange = new PlanChange();
        planChange.setCurrentPlan(currentPlan);
        planChange.setRequestedPlan(requestedPlan);
        planChange.setStatus(null);
        planChange.setRequestedAt(OffsetDateTime.now(ZoneOffset.UTC));
        planChange.setEffectiveAt(billingCycle.getPeriodEndDate().atTime(0,0,0)
                .atOffset(ZoneOffset.UTC));
        planChange.setPhoneLine(phoneLine);

        assertThrows(DataIntegrityViolationException.class, () -> planChangeRepository.saveAndFlush(planChange));
    }

    @Test
    void shouldThrowExceptionWhenPhoneLineIsNull() {
        PlanChange planChange = new PlanChange();
        planChange.setCurrentPlan(currentPlan);
        planChange.setRequestedPlan(requestedPlan);
        planChange.setStatus(PlanChangeStatus.PENDING);
        planChange.setRequestedAt(OffsetDateTime.now(ZoneOffset.UTC));
        planChange.setEffectiveAt(billingCycle.getPeriodEndDate().atTime(0,0,0)
                .atOffset(ZoneOffset.UTC));
        planChange.setPhoneLine(null);

        assertThrows(DataIntegrityViolationException.class, () -> planChangeRepository.saveAndFlush(planChange));
    }

    @Test
    void shouldFindPendingPlanChangesThatAreDue() {

        OffsetDateTime now = OffsetDateTime.now();

        PlanChange planChange = new PlanChange();
        planChange.setStatus(PlanChangeStatus.PENDING);
        planChange.setRequestedPlan(requestedPlan);
        planChange.setCurrentPlan(currentPlan);
        planChange.setPhoneLine(phoneLine);
        planChange.setRequestedAt(now.minusDays(2));
        planChange.setEffectiveAt(now.minusHours(1));
        planChangeRepository.saveAndFlush(planChange);

        PlanChange planChange2 = new PlanChange();
        planChange2.setStatus(PlanChangeStatus.PENDING);
        planChange2.setRequestedPlan(requestedPlan);
        planChange2.setCurrentPlan(currentPlan);
        planChange2.setPhoneLine(phoneLine);
        planChange2.setRequestedAt(now.minusDays(1));
        planChange2.setEffectiveAt(now.plusDays(1));
        planChangeRepository.saveAndFlush(planChange2);

        PlanChange planChange3 = new PlanChange();
        planChange3.setRequestedPlan(requestedPlan);
        planChange3.setCurrentPlan(currentPlan);
        planChange3.setPhoneLine(phoneLine);
        planChange3.setStatus(PlanChangeStatus.COMPLETED);
        planChange3.setRequestedAt(now.minusDays(5));
        planChange3.setEffectiveAt(now.minusDays(3));
        planChangeRepository.saveAndFlush(planChange3);

        List<PlanChange> duePlanChanges = planChangeRepository
                .findByStatusAndEffectiveAtLessThanEqual(PlanChangeStatus.PENDING, now);

        assertEquals(1, duePlanChanges.size());
        assertEquals(PlanChangeStatus.PENDING, duePlanChanges.get(0).getStatus());
        assertTrue(now.isAfter(duePlanChanges.get(0).getEffectiveAt()));
    }
}
