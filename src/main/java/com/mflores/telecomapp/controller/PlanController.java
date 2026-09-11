package com.mflores.telecomapp.controller;

import com.mflores.telecomapp.dto.CreatePlanRequest;
import com.mflores.telecomapp.dto.PlanResponse;
import com.mflores.telecomapp.service.PlanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/plan")
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @PostMapping
    public ResponseEntity<PlanResponse> createPlan(@Valid @RequestBody CreatePlanRequest planRequest) {

        PlanResponse planResponse = planService.createPlan(planRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(planResponse.getPlanId())
                .toUri();

        return ResponseEntity.created(location).body(planResponse);
    }
}
