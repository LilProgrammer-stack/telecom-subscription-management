package com.mflores.telecomapp.dto;

import com.mflores.telecomapp.model.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingCycleResponse {

    private Long billingCycleId;
    private Integer cycleNumber;
    private LocalDate periodStartDate;
    private LocalDate periodEndDate;
    private Account account;
}
