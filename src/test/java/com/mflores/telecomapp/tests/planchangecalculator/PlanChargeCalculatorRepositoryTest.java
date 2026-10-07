package com.mflores.telecomapp.tests.planchangecalculator;

import com.mflores.telecomapp.model.*;
import com.mflores.telecomapp.service.PlanChargeCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PlanChargeCalculatorRepositoryTest {


    private PlanChargeCalculator planChargeCalculator;

    @BeforeEach
    public void setup() {
        planChargeCalculator = new PlanChargeCalculator();
    }

    @Test
    void shouldCalculateProratedPlanChargeWhenPlanEndsDuringBillingCycle(){

        Money planPrice = new Money(10000, Currency.getInstance("USD"));

        LocalDate billingStartDate = LocalDate.of(2024, 5, 10);
        LocalDate billingEndDate = LocalDate.of(2024, 6, 9);

        LocalDate planStartDate = LocalDate.of(2024, 5, 10);
        LocalDate planEndDate = LocalDate.of(2024, 5, 30);

        Money total = planChargeCalculator.calculate(planPrice, billingStartDate, billingEndDate, planStartDate, planEndDate);

        assertEquals(6774, total.amountInCents());
    }

    @Test
    void shouldCalculateProratedPlanChargeWhenPlanStartsAndEndsDuringBillingCycle(){

        Money planPrice = new Money(10000, Currency.getInstance("USD"));

        LocalDate billingStartDate = LocalDate.of(2024, 5, 10);
        LocalDate billingEndDate = LocalDate.of(2024, 6, 9);

        LocalDate planStartDate = LocalDate.of(2024, 5, 15);
        LocalDate planEndDate = LocalDate.of(2024, 5, 30);

        Money total = planChargeCalculator.calculate(planPrice, billingStartDate, billingEndDate, planStartDate, planEndDate);

        assertEquals(5161, total.amountInCents());
    }

    @Test
    void shouldCalculateProratedPlanChargeWhenPlanStartsBeforeAndEndsAfterBillingCycle(){

        Money planPrice = new Money(10000, Currency.getInstance("USD"));

        LocalDate billingStartDate = LocalDate.of(2024, 5, 10);
        LocalDate billingEndDate = LocalDate.of(2024, 6, 9);

        LocalDate planStartDate = LocalDate.of(2024, 4, 15);
        LocalDate planEndDate = LocalDate.of(2024, 7, 30);

        Money total = planChargeCalculator.calculate(planPrice, billingStartDate, billingEndDate, planStartDate, planEndDate);

        assertEquals(10000, total.amountInCents());
    }

    @Test
    void shouldCalculateFullPlanChargeWhenPlanRemainsActive(){

        Money planPrice = new Money(10000, Currency.getInstance("USD"));

        LocalDate billingStartDate = LocalDate.of(2024, 5, 10);
        LocalDate billingEndDate = LocalDate.of(2024, 6, 9);

        LocalDate planStartDate = LocalDate.of(2024, 5, 10);
        LocalDate planEndDate = null;

        Money total = planChargeCalculator.calculate(planPrice, billingStartDate, billingEndDate, planStartDate, planEndDate);

        assertEquals(10000, total.amountInCents());
    }

    @Test
    void shouldCalculateFullPlanChargeWhenPlanStartedBeforeBillingCycleAndRemainsActive(){

        Money planPrice = new Money(10000, Currency.getInstance("USD"));

        LocalDate billingStartDate = LocalDate.of(2024, 5, 10);
        LocalDate billingEndDate = LocalDate.of(2024, 6, 9);

        LocalDate planStartDate = LocalDate.of(2023, 5, 10);
        LocalDate planEndDate = null;

        Money total = planChargeCalculator.calculate(planPrice, billingStartDate, billingEndDate, planStartDate, planEndDate);

        assertEquals(10000, total.amountInCents());
    }
}
