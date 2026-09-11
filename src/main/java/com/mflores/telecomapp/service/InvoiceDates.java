package com.mflores.telecomapp.service;

import java.time.LocalDate;

public record InvoiceDates(
        LocalDate issueDate,
        LocalDate dueDate
) {
}
