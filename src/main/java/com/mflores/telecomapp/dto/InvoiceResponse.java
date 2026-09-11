package com.mflores.telecomapp.dto;

import com.mflores.telecomapp.model.InvoiceStatus;
import com.mflores.telecomapp.model.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponse {

    private Long invoiceId;
    private String invoiceNumber;
    private Money totalAmount;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private InvoiceStatus status;
    private Long billingCycleId;
}
