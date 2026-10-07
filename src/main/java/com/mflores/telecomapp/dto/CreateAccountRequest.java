package com.mflores.telecomapp.dto;

import com.mflores.telecomapp.model.BillingLanguage;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {

    @NotNull(message = "A billing language must be chosen")
    private BillingLanguage billingLanguage;

}
