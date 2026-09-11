package com.mflores.telecomapp.tests.plan.service;

import com.mflores.telecomapp.dto.CreatePlanRequest;
import com.mflores.telecomapp.dto.PlanResponse;
import com.mflores.telecomapp.model.Money;
import com.mflores.telecomapp.model.Plan;
import com.mflores.telecomapp.repository.PlanRepository;
import com.mflores.telecomapp.service.PlanService;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Currency;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTest {

    @Mock
    private PlanRepository planRepository;
    @InjectMocks
    private PlanService planService;

    //The test doesn't currently care about the returned response; it's testing what gets sent to the repository.
    @Test
    void shouldCreatePlanSuccessfully() {
        CreatePlanRequest planRequest = new CreatePlanRequest();
        planRequest.setPlanName("planName");
        planRequest.setDescription("planDescription");
        planRequest.setHotspotLimitMb(10000L);
        planRequest.setRoamingLimitMb(10000L);
        planRequest.setDataLimitMb(10000L);
        Money price = new Money(10000, Currency.getInstance("USD"));
        planRequest.setPrice(price);

        Plan plan = new Plan();
        plan.setPlanId(1L);
        plan.setPlanName("planName");
        plan.setDescription("planDescription");
        plan.setHotspotLimitMb(10000L);
        plan.setRoamingLimitMb(10000L);
        plan.setDataLimitMb(10000L);
        Money priceResponse = new Money(10000, Currency.getInstance("USD"));
        plan.setPrice(priceResponse);
        plan.setActive(true);

        //if this happens, behave this way
        when(planRepository.save(any(Plan.class)))
                .thenReturn(plan);

        planService.createPlan(planRequest);

        ArgumentCaptor<Plan> captor = ArgumentCaptor.forClass(Plan.class);

        //Did this really happened?
        verify(planRepository).save(captor.capture());

        Plan capturedPlan = captor.getValue();

        assertEquals("planName", capturedPlan.getPlanName());
        assertEquals("planDescription", capturedPlan.getDescription());
        assertTrue(capturedPlan.isActive());
        assertEquals(10000L, capturedPlan.getDataLimitMb());
        assertEquals(10000L, capturedPlan.getRoamingLimitMb());
        assertEquals(10000L, capturedPlan.getHotspotLimitMb());
        assertEquals(10000, capturedPlan.getPrice().amountInCents());
        assertEquals(Currency.getInstance("USD"), capturedPlan.getPrice().currency());
    }

    @Test
    void shouldReturnPlanResponseSuccessfully() {
        CreatePlanRequest planRequest = new CreatePlanRequest();
        planRequest.setPlanName("planName");
        planRequest.setDescription("planDescription");
        planRequest.setHotspotLimitMb(10000L);
        planRequest.setRoamingLimitMb(10000L);
        planRequest.setDataLimitMb(10000L);
        Money price = new Money(10000, Currency.getInstance("USD"));
        planRequest.setPrice(price);

        Plan plan = new Plan();
        plan.setPlanId(1L);
        plan.setPlanName("planName");
        plan.setDescription("planDescription");
        plan.setHotspotLimitMb(10000L);
        plan.setRoamingLimitMb(10000L);
        plan.setDataLimitMb(10000L);
        Money priceResponse = new Money(10000, Currency.getInstance("USD"));
        plan.setPrice(priceResponse);
        plan.setActive(true);

        //if this happens, behave this way
        when(planRepository.save(any(Plan.class)))
                .thenReturn(plan);

        PlanResponse planResponse = planService.createPlan(planRequest);

        assertEquals(1L, plan.getPlanId());
        assertEquals("planName", plan.getPlanName());
        assertEquals("planDescription", planResponse.getDescription());
        assertTrue(planResponse.isActive());
        assertEquals(10000L, planResponse.getDataLimitMb());
        assertEquals(10000L, planResponse.getRoamingLimitMb());
        assertEquals(10000L, planResponse.getHotspotLimitMb());
        assertEquals(10000, planResponse.getPrice().amountInCents());
        assertEquals(Currency.getInstance("USD"), planResponse.getPrice().currency());

    }

    @Test
    void shouldNotAllowPlanWithZeroPrice() {
        CreatePlanRequest planRequest = new CreatePlanRequest();
        planRequest.setPlanName("planName");
        planRequest.setDescription("planDescription");
        planRequest.setHotspotLimitMb(10000L);
        planRequest.setRoamingLimitMb(10000L);
        planRequest.setDataLimitMb(10000L);
        Money price = new Money(0, Currency.getInstance("USD"));
        planRequest.setPrice(price);

        assertThrows(IllegalArgumentException.class,() -> planService.createPlan(planRequest));

        verify(planRepository, never()).save(any(Plan.class));
    }
}
