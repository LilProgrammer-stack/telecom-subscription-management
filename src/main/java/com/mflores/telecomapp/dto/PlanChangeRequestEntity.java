package com.mflores.telecomapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanChangeRequestEntity {

    @NotNull(message = "A plan ID is required")
    private Long planId;
}
