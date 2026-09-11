package com.mflores.telecomapp.dto;

import com.mflores.telecomapp.model.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanResponse {

    private Long planId;
    private String planName;
    private String description;
    private Long dataLimitMb;
    private Long roamingLimitMb;
    private Long hotspotLimitMb;
    private boolean active;
    private Money price;

}
