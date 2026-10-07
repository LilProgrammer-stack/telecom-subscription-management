package com.mflores.telecomapp.tests.planchange.service;

import com.mflores.telecomapp.dto.PlanChangeRequestEntity;
import com.mflores.telecomapp.dto.PlanChangeResponse;
import com.mflores.telecomapp.exception.ResourceNotFoundException;
import com.mflores.telecomapp.model.*;
import com.mflores.telecomapp.repository.BillingCycleRepository;
import com.mflores.telecomapp.repository.PlanAssignmentRepository;
import com.mflores.telecomapp.repository.PlanChangeRepository;
import com.mflores.telecomapp.repository.PlanRepository;
import com.mflores.telecomapp.service.PlanChangeService;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PlanChangeServiceTest {

    @Mock
    private PlanChangeRepository planChangeRepository;
    @Mock
    private PlanRepository planRepository;
    @Mock
    private PlanAssignmentRepository planAssignmentRepository;
    @Mock
    private BillingCycleRepository billingCycleRepository;

    @InjectMocks
    private PlanChangeService planChangeService;

    private Account account = new Account();
    Plan plan = new Plan();
    PhoneLine phoneLine = new PhoneLine();
    Plan plan2 = new Plan();
    PlanAssignment  planAssignment = new PlanAssignment();
    BillingCycle  billingCycle = new BillingCycle();
    PlanChange planChange = new PlanChange();
    Long id = 1L;
    Long id2 = 2L;

    @BeforeEach
    public void setup() {
        /* ******* Plan *******/


        plan.setPlanId(id);
        plan.setPlanName("Basic Plan");
        Money price = new Money(7000, Currency.getInstance("USD"));
        plan.setPrice(price);
        plan.setActive(true);
        plan.setDescription("5G network, unlimited data");
        plan.setRoamingLimitMb(0L);
        plan.setHotspotLimitMb(0L);
        plan.setDataLimitMb(null);


        /* ******* PhoneLine *******/

        phoneLine.setPhoneNumber("5555555555");
        phoneLine.setPhoneLineStatus(PhoneLineStatus.ACTIVE);
        phoneLine.setAccount(account);


        /* ******* Plan Assignment Repository *******/


        plan2.setPlanId(id2);
        plan2.setPlanName("Intermediate Plan");
        Money price2 = new Money(7000, Currency.getInstance("USD"));
        plan2.setPrice(price2);
        plan2.setActive(true);
        plan2.setDescription("5G UW network, unlimited data, hotspot included");
        plan2.setRoamingLimitMb(0L);
        plan2.setHotspotLimitMb(100000L);
        plan2.setDataLimitMb(null);


        planAssignment.setPlanAssignmentId(1L);
        planAssignment.setPlan(plan2);
        planAssignment.setPhoneLine(phoneLine);
        planAssignment.setStartDate(LocalDate.of(2002, 11, 13));
        planAssignment.setEndDate(null);


        /* ******* Billing Cycle Repository *******/

        billingCycle.setBillingCycleId(100L);
        billingCycle.setCycleNumber(5);
        billingCycle.setPeriodStartDate(LocalDate.of(2003, 05, 13));
        billingCycle.setPeriodEndDate(LocalDate.of(2003, 06, 12));

        billingCycle.setAccount(account);

        /* ******* PlanChange Repository *******/

        planChange.setCurrentPlan(plan2);
        planChange.setRequestedPlan(plan);
        planChange.setStatus(PlanChangeStatus.PENDING);
        planChange.setPhoneLine(phoneLine);
        planChange.setRequestedAt(OffsetDateTime.of(2003, 05, 13, 04,17, 45,04, ZoneOffset.UTC));
        planChange.setEffectiveAt(OffsetDateTime.of(2003, 06, 13, 00,00, 00,00, ZoneOffset.UTC));

    }

    @Test
    void shouldPerformPlanChangeSuccessfully() {


        PlanChangeRequestEntity planChangeRequestEntity = new PlanChangeRequestEntity();
        planChangeRequestEntity.setPlanId(1L);

        when(planRepository.findById(id))
                .thenReturn(Optional.of(plan));

        when(planAssignmentRepository.findFirstByPhoneLineAndEndDateIsNull(phoneLine))
        .thenReturn(Optional.of(planAssignment));

        when(billingCycleRepository.findFirstByAccountOrderByCycleNumberDesc(phoneLine.getAccount()))
                .thenReturn(Optional.of(billingCycle));

        when(planChangeRepository.save(any(PlanChange.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        OffsetDateTime before = OffsetDateTime.now(ZoneOffset.UTC);

        planChangeService.performPlanChange(planChangeRequestEntity, phoneLine);

        OffsetDateTime after = OffsetDateTime.now(ZoneOffset.UTC);

        ArgumentCaptor <PlanChange> captor = ArgumentCaptor.forClass(PlanChange.class);

        verify(planChangeRepository).save(captor.capture());

        PlanChange planChangeValue = captor.getValue();
        assertNotNull(planChangeValue.getRequestedAt());
        assertFalse(planChangeValue.getRequestedAt().isBefore(before));
        assertFalse(planChangeValue.getRequestedAt().isAfter(after));
        assertEquals(PlanChangeStatus.PENDING, planChangeValue.getStatus());
        assertEquals(phoneLine, planChangeValue.getPhoneLine());
        assertEquals(plan2, planChangeValue.getCurrentPlan());
        assertEquals(plan,  planChangeValue.getRequestedPlan());
        assertEquals(
                LocalDate.of(2003, 6, 13)
                        .atStartOfDay()
                        .atOffset(ZoneOffset.UTC),
                planChangeValue.getEffectiveAt()
        );

    }

    @Test
    void shouldReturnPlanChangeResponse() {

        PlanChangeRequestEntity planChangeRequestEntity = new PlanChangeRequestEntity();
        planChangeRequestEntity.setPlanId(1L);

        when(planRepository.findById(id))
                .thenReturn(Optional.of(plan));

        when(planAssignmentRepository.findFirstByPhoneLineAndEndDateIsNull(phoneLine))
                .thenReturn(Optional.of(planAssignment));

        when(billingCycleRepository.findFirstByAccountOrderByCycleNumberDesc(phoneLine.getAccount()))
                .thenReturn(Optional.of(billingCycle));

        when(planChangeRepository.save(any(PlanChange.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        OffsetDateTime before = OffsetDateTime.now(ZoneOffset.UTC);

        PlanChangeResponse response = planChangeService.performPlanChange(planChangeRequestEntity, phoneLine);

        OffsetDateTime after = OffsetDateTime.now(ZoneOffset.UTC);

        assertNotNull(response.getRequestedAt());
        assertFalse(response.getRequestedAt().isBefore(before));
        assertFalse(response.getRequestedAt().isAfter(after));
        assertEquals(PlanChangeStatus.PENDING, response.getStatus());
        assertEquals(phoneLine.getPhoneLineId(), response.getPhoneLineId());
        assertEquals(plan2, response.getCurrentPlan());
        assertEquals(plan,  response.getRequestedPlan());


        verify(planChangeRepository).save(any(PlanChange.class));

    }


    @Test
    void shouldThrowExceptionWhenRequestedPlanDoesNotExist(){

        Long id = 100000L;
        PlanChangeRequestEntity planChangeRequestEntity = new PlanChangeRequestEntity();
        planChangeRequestEntity.setPlanId(id);

        when(planRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> planChangeService.performPlanChange(planChangeRequestEntity, phoneLine));

        verify(planChangeRepository, never()).save(any(PlanChange.class));
    }

    @Test
    void shouldThrowExceptionWhenThereIsNoActivePlanAssignment(){

        PlanChangeRequestEntity planChangeRequestEntity = new PlanChangeRequestEntity();
        planChangeRequestEntity.setPlanId(id);

        when(planRepository.findById(id))
                .thenReturn(Optional.of(plan));

        when(planAssignmentRepository.findFirstByPhoneLineAndEndDateIsNull(phoneLine))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> planChangeService.performPlanChange(planChangeRequestEntity, phoneLine));
        verify(planChangeRepository, never()).save(any(PlanChange.class));
    }

    @Test
    void shouldThrowExceptionWhenThereIsNoBillingCycle(){
        PlanChangeRequestEntity planChangeRequestEntity = new PlanChangeRequestEntity();
        planChangeRequestEntity.setPlanId(id);

        when(planRepository.findById(id))
                .thenReturn(Optional.of(plan));

        when(planAssignmentRepository.findFirstByPhoneLineAndEndDateIsNull(phoneLine))
        .thenReturn(Optional.of(planAssignment));

        when(billingCycleRepository.findFirstByAccountOrderByCycleNumberDesc(phoneLine.getAccount()))
        .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> planChangeService.performPlanChange(planChangeRequestEntity, phoneLine));

        verify(planChangeRepository, never()).save(any(PlanChange.class));
    }

    @Test
    void shouldThrowExceptionWhenRequestedPlanIsTheSameAsCurrentPlan(){

        PlanChangeRequestEntity planChangeRequestEntity = new PlanChangeRequestEntity();
        planChangeRequestEntity.setPlanId(id);

        planAssignment.setPlan(plan);

        when(planRepository.findById(id))
                .thenReturn(Optional.of(plan));

        when(planAssignmentRepository.findFirstByPhoneLineAndEndDateIsNull(phoneLine))
                .thenReturn(Optional.of(planAssignment));

        assertThrows(IllegalArgumentException.class, () -> planChangeService.performPlanChange(planChangeRequestEntity, phoneLine));

        verify(planChangeRepository, never()).save(any(PlanChange.class));

    }

    @Test
    void shouldNotExecutePlanChangeWhenStatusIsNotPending() {

        PlanChange planChange = new PlanChange();
        planChange.setStatus(PlanChangeStatus.COMPLETED);

        assertThrows(IllegalStateException.class, () ->  planChangeService.executePlanChange(planChange));
    }

    @Test
    void shouldThrowExceptionWhenNoActivePlanAssignmentExists() {

        PlanChange planChange = new PlanChange();
        planChange.setStatus(PlanChangeStatus.PENDING);
        planChange.setPhoneLine(phoneLine);

        when(planAssignmentRepository.findFirstByPhoneLineAndEndDateIsNull(phoneLine))
        .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> planChangeService.executePlanChange(planChange));

        verify(planAssignmentRepository)
                .findFirstByPhoneLineAndEndDateIsNull(phoneLine);

        verify(planChangeRepository, never()).save(any());
    }

    @Test
    void shouldCompletePlanChange() {

        PlanChange planChange = new PlanChange();
        planChange.setStatus(PlanChangeStatus.PENDING);
        planChange.setPhoneLine(phoneLine);
        planChange.setEffectiveAt(
                OffsetDateTime.of(2003, 7, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        );

        //we check which plan assignment is the most recent (historical records) for the provided phone line
        when(planAssignmentRepository.findFirstByPhoneLineAndEndDateIsNull(phoneLine))
        .thenReturn(Optional.of(planAssignment));

        //we pass the targeted plan change into the save method
        when(planChangeRepository.save(planChange))
                .thenAnswer(i -> i.getArgument(0));

        planChangeService.executePlanChange(planChange);

        assertEquals(PlanChangeStatus.COMPLETED, planChange.getStatus());

        verify(planAssignmentRepository).findFirstByPhoneLineAndEndDateIsNull(phoneLine);
        verify(planChangeRepository).save(planChange);
    }

    @Test
    void shouldCreateNewPlanAssignment() {

        PlanChange planChange = new PlanChange();
        planChange.setStatus(PlanChangeStatus.PENDING);
        planChange.setPhoneLine(phoneLine);
        planChange.setEffectiveAt(
                OffsetDateTime.of(2003, 7, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        );
        planChange.setRequestedPlan(plan);
        planChange.setCurrentPlan(plan2);

        when(planAssignmentRepository.findFirstByPhoneLineAndEndDateIsNull(planChange.getPhoneLine()))
                .thenReturn(Optional.of(planAssignment));

        when(planChangeRepository.save(planChange))
        .thenAnswer(i -> i.getArgument(0));

        planChangeService.executePlanChange(planChange);

        ArgumentCaptor<PlanAssignment> planAssignmentCaptor = ArgumentCaptor.forClass(PlanAssignment.class);

        verify(planAssignmentRepository,times(2)).save(planAssignmentCaptor.capture());

        List<PlanAssignment> savedPlanAssignments = planAssignmentCaptor.getAllValues();

        PlanAssignment previousAssignment = savedPlanAssignments.get(0);
        PlanAssignment createdAssignment = savedPlanAssignments.get(1);

        assertEquals(planChange.getEffectiveAt().toLocalDate().minusDays(1), previousAssignment.getEndDate());
        assertEquals(planChange.getCurrentPlan(), previousAssignment.getPlan());
        assertEquals(planChange.getRequestedPlan(), createdAssignment.getPlan());
        assertEquals(planChange.getPhoneLine(), createdAssignment.getPhoneLine());
        assertNull(createdAssignment.getEndDate());

        verify(planAssignmentRepository).findFirstByPhoneLineAndEndDateIsNull(phoneLine);
    }
}
