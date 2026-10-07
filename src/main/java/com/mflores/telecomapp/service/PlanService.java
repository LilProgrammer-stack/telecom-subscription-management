package com.mflores.telecomapp.service;

import com.mflores.telecomapp.dto.CreatePlanRequest;
import com.mflores.telecomapp.dto.PlanResponse;
import com.mflores.telecomapp.model.Plan;
import com.mflores.telecomapp.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.Currency;

@Service
public class PlanService {

    private final PlanRepository planRepository;
    private static final Currency PLAN_CURRENCY = Currency.getInstance("USD");

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public PlanResponse createPlan(CreatePlanRequest planRequest) {

        if (!PLAN_CURRENCY.equals(planRequest.getPrice().currency())) {
            throw new IllegalArgumentException("Plan price must be in USD");
        }

        Plan plan = new Plan();
        plan.setPrice(planRequest.getPrice());
        plan.setPlanName(planRequest.getPlanName());
        plan.setDescription(planRequest.getDescription());
        plan.setHotspotLimitMb(planRequest.getHotspotLimitMb());
        plan.setRoamingLimitMb(planRequest.getRoamingLimitMb());
        plan.setDataLimitMb(planRequest.getDataLimitMb());

        plan.setActive(true);
        Plan savedPlan = planRepository.save(plan);
        return convertIntoPlanResponse(savedPlan);
    }



    private PlanResponse convertIntoPlanResponse(Plan plan) {
        return new PlanResponse(plan.getPlanId(), plan.getPlanName(), plan.getDescription(),
                plan.getDataLimitMb(), plan.getRoamingLimitMb(), plan.getHotspotLimitMb(),
                plan.isActive(), plan.getPrice());
    }
}
