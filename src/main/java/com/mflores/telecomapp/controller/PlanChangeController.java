package com.mflores.telecomapp.controller;

import com.mflores.telecomapp.dto.PlanChangeRequestEntity;
import com.mflores.telecomapp.dto.PlanChangeResponse;
import com.mflores.telecomapp.model.PhoneLine;
import com.mflores.telecomapp.service.PhoneLineService;
import com.mflores.telecomapp.service.PlanChangeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/phone-lines/{phoneLineId}/plan-changes")
public class PlanChangeController {


    private final PlanChangeService planChangeService;
    private final PhoneLineService phoneLineService;

    public PlanChangeController(PlanChangeService planChangeService, PhoneLineService phoneLineService) {
        this.planChangeService = planChangeService;
        this.phoneLineService = phoneLineService;
    }

    @PostMapping
    public ResponseEntity<PlanChangeResponse> performPlanChange(@Valid @RequestBody PlanChangeRequestEntity planChangeRequest,
                                                                @PathVariable Long phoneLineId){

        PhoneLine foundPhoneLine = phoneLineService.findPhoneLineEntityById(phoneLineId);
        PlanChangeResponse response = planChangeService.performPlanChange(planChangeRequest, foundPhoneLine);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{planChangeId}")
                .buildAndExpand(response.getPlanChangeId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }


}
