package com.mflores.telecomapp.service;

import com.mflores.telecomapp.model.Money;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Service
public class PlanChargeCalculator {

    public Money calculate(Money pricePlan, LocalDate billingStartDate, LocalDate billingEndDate,
                           LocalDate planStartDate, LocalDate planEndDate) {

        LocalDate effectiveStartDate =
                billingStartDate.isAfter(planStartDate)
                        ? billingStartDate
                        : planStartDate;

        LocalDate effectiveEndDate;

        if (planEndDate == null) {
            effectiveEndDate = billingEndDate;
        }else {
            effectiveEndDate =
                    billingEndDate.isBefore(planEndDate)
                            ? billingEndDate
                            : planEndDate;
        }

        Long daysBetweenPlan =  ChronoUnit.DAYS.between(effectiveStartDate, effectiveEndDate)+1;

        Long daysBetweenBilling = ChronoUnit.DAYS.between(billingStartDate, billingEndDate)+1;

        Long total = pricePlan.amountInCents()*daysBetweenPlan/daysBetweenBilling;

        return new Money(total, pricePlan.currency());
    }
}
