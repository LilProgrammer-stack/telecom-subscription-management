package com.mflores.telecomapp.service;

import com.mflores.telecomapp.dto.CreatePlanRequest;
import com.mflores.telecomapp.dto.PlanResponse;
import com.mflores.telecomapp.model.Plan;
import com.mflores.telecomapp.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanService {

    private final PlanRepository planRepository;

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public PlanResponse createPlan(CreatePlanRequest planRequest) {

        if (planRequest.getPrice().amountInCents() <= 0) {
            throw new IllegalArgumentException("Plan price must be greater than zero");
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
