package com.mflores.telecomapp.dto;

import com.mflores.telecomapp.model.BillingLanguage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {

    private BillingLanguage billingLanguage;

}
