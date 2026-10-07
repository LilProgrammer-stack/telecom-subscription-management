package com.mflores.telecomapp.scheduler;

import com.mflores.telecomapp.dto.PlanChangeResponse;
import com.mflores.telecomapp.model.PlanChange;
import com.mflores.telecomapp.model.PlanChangeStatus;
import com.mflores.telecomapp.repository.PlanChangeRepository;
import com.mflores.telecomapp.service.PlanChangeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;


@Component
public class PlanChangeScheduler {

    private final PlanChangeRepository planChangeRepository;
    private final PlanChangeService planChangeService;

    public PlanChangeScheduler(PlanChangeRepository planChangeRepository, PlanChangeService planChangeService) {
        this.planChangeRepository = planChangeRepository;
        this.planChangeService = planChangeService;
    }

    @Scheduled(fixedRate = 60000)
    public void processPendingPlanChanges() {

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        planChangeRepository
                .findByStatusAndEffectiveAtLessThanEqual(PlanChangeStatus.PENDING, now)
                .forEach(planChangeService::executePlanChange);

    }
}
