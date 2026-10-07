package com.mflores.telecomapp.tests.planassignment.repository;

import com.mflores.telecomapp.exception.ResourceNotFoundException;
import com.mflores.telecomapp.model.*;

import static org.junit.jupiter.api.Assertions.*;

import com.mflores.telecomapp.repository.*;
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
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlanAssignmentRepositoryTest {

    @Autowired
    private PlanAssignmentRepository planAssignmentRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private PhoneLineRepository phoneLineRepository;

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountOwnerRepository accountOwnerRepository;
    @Autowired
    private BillingCycleRepository billingCycleRepository;

    private AccountOwner accountOwner = new AccountOwner();
    private Account account = new Account();
    private PhoneLine phoneLine  = new PhoneLine();
    private Plan plan = new Plan();
    private LocalDate startDate = LocalDate.of(2026, 9, 13);
    private LocalDate endDate = startDate.plusDays(30);

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

        phoneLine.setAccount(account);
        phoneLine.setPhoneNumber("1555555555");
        phoneLine.setPhoneLineStatus(PhoneLineStatus.ACTIVE);
        phoneLineRepository.save(phoneLine);

        plan.setPlanName("Unlimited Welcome");
        plan.setDescription("Unlimited 5G data, 25 GB of roaming, 200GB of hotspot");
        plan.setActive(true);
        plan.setDataLimitMb(null);
        plan.setRoamingLimitMb(25600L);
        plan.setHotspotLimitMb(204800L);
        Money money = new Money(10000, Currency.getInstance("USD"));
        plan.setPrice(money);
        planRepository.saveAndFlush(plan);


    }

    @Test
    void shouldCreatePlanAssignmentSuccessfully() {

        PlanAssignment planAssignment = new PlanAssignment();
        planAssignment.setPlan(plan);
        planAssignment.setPhoneLine(phoneLine);
        planAssignment.setStartDate(startDate);
        planAssignment.setEndDate(endDate);

        PlanAssignment assignment = planAssignmentRepository.saveAndFlush(planAssignment);
        assertNotNull(assignment.getPlanAssignmentId());

        PlanAssignment foundPlanAssignment = planAssignmentRepository.findById(assignment.getPlanAssignmentId())
                .orElseThrow(()-> new ResourceNotFoundException("Plan Assignment not found with id: " + assignment.getPlanAssignmentId()));

        assertEquals(startDate, foundPlanAssignment.getStartDate());
        assertEquals(endDate, foundPlanAssignment.getEndDate());
        assertEquals("Unlimited Welcome", foundPlanAssignment.getPlan().getPlanName());
        assertEquals("Unlimited 5G data, 25 GB of roaming, 200GB of hotspot",  foundPlanAssignment.getPlan().getDescription());
        assertEquals("1555555555", foundPlanAssignment.getPhoneLine().getPhoneNumber());
        assertEquals(PhoneLineStatus.ACTIVE, foundPlanAssignment.getPhoneLine().getPhoneLineStatus());

        assertEquals("12346test", foundPlanAssignment.getPhoneLine().getAccount().getAccountNumber());
        assertEquals("John", foundPlanAssignment.getPhoneLine().getAccount().getAccountOwner().getFirstName());
        assertEquals("Smith", foundPlanAssignment.getPhoneLine().getAccount().getAccountOwner().getLastName());
    }

    @Test
    void shouldNotAllowNullPhoneLine() {

        PlanAssignment planAssignment = new PlanAssignment();
        planAssignment.setStartDate(startDate);
        planAssignment.setEndDate(endDate);
        planAssignment.setPlan(plan);
        planAssignment.setPhoneLine(null);

        assertThrows(DataIntegrityViolationException.class, () -> planAssignmentRepository.saveAndFlush(planAssignment));
    }

    @Test
    void shouldNotAllowNullPlan() {

        PlanAssignment planAssignment = new PlanAssignment();
        planAssignment.setStartDate(startDate);
        planAssignment.setEndDate(endDate);
        planAssignment.setPhoneLine(phoneLine);
        planAssignment.setPlan(null);

        assertThrows(DataIntegrityViolationException.class, () -> planAssignmentRepository.saveAndFlush(planAssignment));
    }

    @Test
    void shouldNotAllowNullStartDate() {
        PlanAssignment planAssignment = new PlanAssignment();
        planAssignment.setStartDate(null);
        planAssignment.setEndDate(endDate);
        planAssignment.setPhoneLine(phoneLine);
        planAssignment.setPlan(plan);

        assertThrows(DataIntegrityViolationException.class, () -> planAssignmentRepository.saveAndFlush(planAssignment));}

    @Test
    void shouldAllowNullEndDate() {
        PlanAssignment planAssignment = new PlanAssignment();
        planAssignment.setStartDate(startDate);
        planAssignment.setEndDate(null);
        planAssignment.setPhoneLine(phoneLine);
        planAssignment.setPlan(plan);

        PlanAssignment foundPlanAssignment = planAssignmentRepository.saveAndFlush(planAssignment);
        assertNull(foundPlanAssignment.getEndDate());

    }

    @Test
    void shouldReturnActivePlan() {
        PlanAssignment planAssignment = new PlanAssignment();
        planAssignment.setStartDate(startDate);
        planAssignment.setEndDate(endDate);
        planAssignment.setPhoneLine(phoneLine);
        planAssignment.setPlan(plan);

        planAssignmentRepository.saveAndFlush(planAssignment);

        PlanAssignment planAssignment2 = new PlanAssignment();
        planAssignment2.setStartDate(LocalDate.of(2026, 9, 14));
        planAssignment2.setEndDate(null);
        planAssignment2.setPhoneLine(phoneLine);
        planAssignment2.setPlan(plan);

        planAssignmentRepository.saveAndFlush(planAssignment2);

        Optional<PlanAssignment> foundPlanAssignment = planAssignmentRepository.findFirstByPhoneLineAndEndDateIsNull(phoneLine);

        assertEquals(LocalDate.of(2026, 9, 14), foundPlanAssignment.get().getStartDate());
        assertEquals(plan, foundPlanAssignment.get().getPlan());
        assertEquals(phoneLine, foundPlanAssignment.get().getPhoneLine());
        assertNull(foundPlanAssignment.get().getEndDate());

    }

    @Test
    void shouldGetPlanAssignmentBasedOnBillingCycle() {

        BillingCycle billingCycle = new BillingCycle();
        billingCycle.setCycleNumber(1);
        billingCycle.setAccount(account);
        billingCycle.setPeriodStartDate(LocalDate.of(2026, 9, 14));
        billingCycle.setPeriodEndDate(LocalDate.of(2026, 10, 13));

        billingCycleRepository.saveAndFlush(billingCycle);

        PlanAssignment planAssignment = new PlanAssignment();
        planAssignment.setPlan(plan);
        planAssignment.setPhoneLine(phoneLine);
        planAssignment.setStartDate(LocalDate.of(2026, 9, 20));
        planAssignment.setEndDate(null);
        planAssignmentRepository.saveAndFlush(planAssignment);


        PlanAssignment planAssignment2 = new PlanAssignment();
        planAssignment2.setPlan(plan);
        planAssignment2.setPhoneLine(phoneLine);
        planAssignment2.setStartDate(LocalDate.of(2026, 8, 20));
        planAssignment2.setEndDate(null);
        planAssignmentRepository.saveAndFlush(planAssignment2);

        PlanAssignment planAssignment3 = new PlanAssignment();
        planAssignment3.setPlan(plan);
        planAssignment3.setPhoneLine(phoneLine);
        planAssignment3.setStartDate(LocalDate.of(2026, 9, 20));
        planAssignment3.setEndDate(LocalDate.of(2026, 9, 29));
        planAssignmentRepository.saveAndFlush(planAssignment3);

        List<PlanAssignment> foundPlanAssignments = planAssignmentRepository.findAssignmentsForBillingCycle(billingCycle.getPeriodStartDate(),
                billingCycle.getPeriodEndDate());

        assertEquals(3, foundPlanAssignments.size());

        assertTrue(foundPlanAssignments.contains(planAssignment));
        assertTrue(foundPlanAssignments.contains(planAssignment2));
        assertTrue(foundPlanAssignments.contains(planAssignment3));
    }

    @Test
    void shouldReturnEmptyListWhenPlanAssignmentDoesNotOverlapWithCurrentBillingCycle() {

        BillingCycle billingCycle = new BillingCycle();
        billingCycle.setCycleNumber(1);
        billingCycle.setAccount(account);
        billingCycle.setPeriodStartDate(LocalDate.of(2026, 9, 14));
        billingCycle.setPeriodEndDate(LocalDate.of(2026, 10, 13));

        billingCycleRepository.saveAndFlush(billingCycle);

        PlanAssignment planAssignment = new PlanAssignment();
        planAssignment.setPlan(plan);
        planAssignment.setPhoneLine(phoneLine);
        planAssignment.setStartDate(LocalDate.of(2026, 12, 20));
        planAssignment.setEndDate(null);
        planAssignmentRepository.saveAndFlush(planAssignment);

        List<PlanAssignment> foundPlanAssignments = planAssignmentRepository.findAssignmentsForBillingCycle(billingCycle.getPeriodStartDate(),
                billingCycle.getPeriodEndDate());

        assertEquals(0,  foundPlanAssignments.size());
    }


}
