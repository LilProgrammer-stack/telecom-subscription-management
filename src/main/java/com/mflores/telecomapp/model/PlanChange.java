package com.mflores.telecomapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "plan_change")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_change_id", nullable = false)
    private Long planChangeId;
    @ManyToOne
    @JoinColumn(name = "current_plan_id", nullable = false)
    private Plan currentPlan;
    @ManyToOne
    @JoinColumn(name = "requested_plan_id", nullable = false)
    private Plan requestedPlan;
    @Column(nullable = false)
    private OffsetDateTime requestedAt;
    @Column(nullable = false)
    private  OffsetDateTime effectiveAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PlanChangeStatus status;
    @ManyToOne
    @JoinColumn(name = "phone_line_id", nullable = false)
    private PhoneLine phoneLine;
}
