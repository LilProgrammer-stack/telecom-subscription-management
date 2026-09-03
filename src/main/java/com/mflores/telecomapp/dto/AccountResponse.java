package com.mflores.telecomapp.dto;

import com.mflores.telecomapp.model.AccountStatus;
import com.mflores.telecomapp.model.BillingLanguage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    private Long accountId;
    private String accountNumber;
    private AccountStatus accountStatus;
    private BillingLanguage billingLanguage;
    private OffsetDateTime creationDate;
}
