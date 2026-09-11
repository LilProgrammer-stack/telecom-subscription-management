package com.mflores.telecomapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long planId;
    @Column(nullable = false, unique = true, length = 255)
    private String planName;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "amountInCents",
                    column = @Column(name = "price")
            ),
            @AttributeOverride(
                    name = "currency",
                    column = @Column(name = "currency")
            )
    })
    private Money price;
    @Column(name = "data_limit_mb")
    private Long dataLimitMb;
    @Column(name = "roaming_limit_mb")
    private Long roamingLimitMb;
    @Column(name = "hotspot_limit_mb")
    private Long hotspotLimitMb;
    @Column(nullable = false, length = 255)
    private String description;
    @Column(nullable = false)
    private boolean active;


}
