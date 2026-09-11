package com.mflores.telecomapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;
    @Column(length = 20, nullable = false, unique = true)
    private String invoiceNumber;
    @Embedded
    private Money totalAmount;
    @Column(nullable = false)
    private LocalDate issueDate;
    @Column(nullable = false)
    private LocalDate dueDate;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;
    @OneToOne
    @JoinColumn(name = "billing_cycle_id", nullable = false, unique = true)
    private BillingCycle billingCycle;
}
