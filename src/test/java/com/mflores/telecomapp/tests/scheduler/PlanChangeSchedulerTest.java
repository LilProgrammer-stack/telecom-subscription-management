package com.mflores.telecomapp.tests.scheduler;

import com.mflores.telecomapp.model.*;
import com.mflores.telecomapp.repository.*;
import com.mflores.telecomapp.scheduler.PlanChangeScheduler;
import com.mflores.telecomapp.service.PlanChangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Currency;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanChangeSchedulerTest {

    @Mock
    private PlanChangeRepository planChangeRepository;

    @Mock
    private PlanChangeService planChangeService;

    @Mock
    private PlanRepository planRepository;
    @Mock
    private PhoneLineRepository phoneLineRepository;

    @Mock
    private AccountOwnerRepository accountOwnerRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private BillingCycleRepository billingCycleRepository;

    @InjectMocks
    private PlanChangeScheduler planChangeScheduler;


    private AccountOwner accountOwner = new AccountOwner();
    private Account account = new Account();
    private BillingCycle billingCycle = new BillingCycle();

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
    void shouldExecuteDuePlanChanges() {

        OffsetDateTime now = OffsetDateTime.now();

        PlanChange planChange = new PlanChange();
        planChange.setRequestedPlan(requestedPlan);
        planChange.setCurrentPlan(currentPlan);
        planChange.setStatus(PlanChangeStatus.PENDING);
        planChange.setPhoneLine(phoneLine);
        planChange.setRequestedAt(now.minusDays(5));
        planChange.setEffectiveAt(now.minusDays(1));

        PlanChange planChange2 = new PlanChange();
        planChange2.setRequestedPlan(requestedPlan);
        planChange2.setCurrentPlan(currentPlan);
        planChange2.setStatus(PlanChangeStatus.PENDING);
        planChange2.setPhoneLine(phoneLine);
        planChange2.setRequestedAt(now.minusDays(5));
        planChange2.setEffectiveAt(now);

        when(planChangeRepository.findByStatusAndEffectiveAtLessThanEqual(eq(PlanChangeStatus.PENDING), any(OffsetDateTime.class)))
                .thenReturn(List.of(planChange, planChange2));

        planChangeScheduler.processPendingPlanChanges();

        verify(planChangeService).executePlanChange(planChange);
        verify(planChangeService).executePlanChange(planChange2);
    }


}
