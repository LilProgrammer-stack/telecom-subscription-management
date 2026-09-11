package com.mflores.telecomapp.dto;

import com.mflores.telecomapp.model.Money;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlanRequest {

    @NotBlank(message = "Plan name is required")
    @Size(max = 255, message = "Plan name cannot exceed 255 characters")
    private String planName;
    @NotNull(message = "A price is required")
    private Money price;
    @PositiveOrZero(message = "Data cannot be negative")
    private Long dataLimitMb;
    @PositiveOrZero(message = "Roaming cannot be negative")
    private Long roamingLimitMb;
    @PositiveOrZero(message = "Hotspot cannot be negative")
    private Long hotspotLimitMb;
    @NotBlank(message = "A description is required")
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
}
