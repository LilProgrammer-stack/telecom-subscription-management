package com.mflores.telecomapp.dto;

import com.mflores.telecomapp.model.Account;
import com.mflores.telecomapp.model.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DevicePaymentAgreementResponse {

    private Long devicePaymentAgreementId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Money financedAmount;
    private Integer totalInstallments;
    private Integer paidInstallments;
    private Account account;

}
