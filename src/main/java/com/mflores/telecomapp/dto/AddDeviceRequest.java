package com.mflores.telecomapp.dto;

import com.mflores.telecomapp.model.BrandType;
import com.mflores.telecomapp.model.Money;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddDeviceRequest {

    @NotBlank(message = "A device model is required")
    private String model;
    @NotBlank(message = "An IMEI number is required")
    @Pattern(
            regexp = "^\\d{15}$",
            message = "IMEI must contain exactly 15 digits."
    )
    private String imei;
    @NotNull(message = "A device brand type is required")
    private BrandType brandType;
    @NotNull(message = "A price is required")
    private Money price;
    @NotNull(message = "Financing eligibility must be specified")
    private Boolean financingEligible;
}
