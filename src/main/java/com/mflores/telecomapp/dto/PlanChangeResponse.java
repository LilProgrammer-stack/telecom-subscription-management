package com.mflores.telecomapp.dto;

import com.mflores.telecomapp.model.PhoneLine;
import com.mflores.telecomapp.model.Plan;
import com.mflores.telecomapp.model.PlanChangeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanChangeResponse {

    private Long planChangeId;
    private Plan currentPlan;
    private Plan requestedPlan;
    private OffsetDateTime requestedAt;
    private  OffsetDateTime effectiveAt;
    private PlanChangeStatus status;
    private Long phoneLineId;
}
