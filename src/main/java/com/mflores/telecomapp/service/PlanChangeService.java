package com.mflores.telecomapp.service;

import com.mflores.telecomapp.dto.PlanChangeRequestEntity;
import com.mflores.telecomapp.dto.PlanChangeResponse;
import com.mflores.telecomapp.exception.ResourceNotFoundException;
import com.mflores.telecomapp.model.*;
import com.mflores.telecomapp.repository.BillingCycleRepository;
import com.mflores.telecomapp.repository.PlanAssignmentRepository;
import com.mflores.telecomapp.repository.PlanChangeRepository;
import com.mflores.telecomapp.repository.PlanRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class PlanChangeService {

    private final PlanChangeRepository planChangeRepository;
    private final PlanRepository planRepository;
    private final PlanAssignmentRepository planAssignmentRepository;
    private final BillingCycleRepository billingCycleRepository;

    public PlanChangeService(PlanChangeRepository planChangeRepository,  PlanRepository planRepository,
                             PlanAssignmentRepository planAssignmentRepository,  BillingCycleRepository billingCycleRepository) {
        this.planChangeRepository = planChangeRepository;
        this.planRepository = planRepository;
        this.planAssignmentRepository = planAssignmentRepository;
        this.billingCycleRepository = billingCycleRepository;
    }

    public PlanChangeResponse performPlanChange(PlanChangeRequestEntity planChangeRequestEntity, PhoneLine  phoneLine) {

        Plan requestedPlan;
        PlanAssignment foundPlanAssignment;

        Optional<Plan> foundPlan = this.planRepository.findById(planChangeRequestEntity.getPlanId());
        if (foundPlan.isPresent()) {
            requestedPlan = foundPlan.get();
        }else{
            throw new ResourceNotFoundException("Plan not found");
        }

        Optional<PlanAssignment> planAssignment = this.planAssignmentRepository.findFirstByPhoneLineAndEndDateIsNull(phoneLine);
        if (planAssignment.isPresent()) {
            foundPlanAssignment = planAssignment.get();
        }else{
            throw new ResourceNotFoundException("Plan assignment not found");
        }

        Plan getCurrentPlan = foundPlanAssignment.getPlan();

        if (requestedPlan.getPlanId().equals(getCurrentPlan.getPlanId())) {
            throw new IllegalArgumentException("Plan "+getCurrentPlan.getPlanName()+" is already active on the account");
        }

        Optional<BillingCycle> currentBillingCycle = billingCycleRepository.
                findFirstByAccountOrderByCycleNumberDesc(phoneLine.getAccount());
        OffsetDateTime effectiveAt;
        if (currentBillingCycle.isPresent()) {
            effectiveAt = currentBillingCycle.get().getPeriodEndDate()
                    .plusDays(1)
                    .atStartOfDay()
                    .atOffset(ZoneOffset.UTC);
        }else{
            throw new ResourceNotFoundException("Billing cycle not found");
        }

        PlanChange planChange = new PlanChange();
        planChange.setCurrentPlan(getCurrentPlan);
        planChange.setRequestedPlan(requestedPlan);
        planChange.setPhoneLine(phoneLine);
        planChange.setStatus(PlanChangeStatus.PENDING);
        planChange.setRequestedAt(OffsetDateTime.now(ZoneOffset.UTC));
        planChange.setEffectiveAt(effectiveAt);


        PlanChange savedPlanChange = planChangeRepository.save(planChange);

        return convertIntoPlanChangeResponse(savedPlanChange);
    }

    @Transactional
    public PlanChangeResponse executePlanChange(PlanChange planChange){

        if (planChange.getStatus() != PlanChangeStatus.PENDING) {
            throw new IllegalStateException("Only pending plan changes can be executed");
        }

        Optional<PlanAssignment> currentPlanAssignment = planAssignmentRepository.findFirstByPhoneLineAndEndDateIsNull(planChange.getPhoneLine());
        if (currentPlanAssignment.isEmpty()){
            throw new ResourceNotFoundException("There's no active current plan");
        }

        PlanAssignment currentPlanAssignmentEntity = currentPlanAssignment.get();

        LocalDate endDate = planChange.getEffectiveAt().toLocalDate().minusDays(1);

        currentPlanAssignmentEntity.setEndDate(endDate);
        planAssignmentRepository.save(currentPlanAssignmentEntity);

        planChange.setStatus(PlanChangeStatus.COMPLETED);
        PlanChange savedPlanChange = planChangeRepository.save(planChange);

        PlanAssignment newPlanAssignment = new PlanAssignment();
        newPlanAssignment.setPhoneLine(planChange.getPhoneLine());
        newPlanAssignment.setPlan(planChange.getRequestedPlan());
        newPlanAssignment.setStartDate(planChange.getEffectiveAt().toLocalDate());
        newPlanAssignment.setEndDate(null);

        planAssignmentRepository.save(newPlanAssignment);
        return convertIntoPlanChangeResponse(savedPlanChange);
    }


    private PlanChangeResponse convertIntoPlanChangeResponse(PlanChange planChange){
        return new PlanChangeResponse(planChange.getPlanChangeId(), planChange.getCurrentPlan(),
                planChange.getRequestedPlan(), planChange.getRequestedAt(), planChange.getEffectiveAt(),
                planChange.getStatus(), planChange.getPhoneLine().getPhoneLineId());
    }


}
