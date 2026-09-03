package com.mflores.telecomapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "billing_cycle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "billing_cycle_id", nullable = false)
    private Long billingCycleId;
    @Column(name = "cycle_number", nullable = false, unique = true)
    private Integer cycleNumber;
    @Column(name = "period_start_date", nullable = false)
    private LocalDate periodStartDate;
    @Column(name = "period_end_date", nullable = false)
    private LocalDate periodEndDate;
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public BillingCycle(Integer cycleNumber, LocalDate periodStartDate, LocalDate periodEndDate, LocalDate dueDate, Account account) {
        this.cycleNumber = cycleNumber;
        this.periodStartDate = periodStartDate;
        this.periodEndDate = periodEndDate;
        this.dueDate = dueDate;
        this.account = account;
    }
}
