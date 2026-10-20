package com.mflores.telecomapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "device")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long deviceId;
    @Column(nullable = false)
    private String model;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BrandType brand;
    @Column(nullable = false, unique = true)
    private String imei;
    @Column(nullable = false)
    private Boolean locked;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "amountInCents",
                    column = @Column(name = "price_amount")
            ),
            @AttributeOverride(
                    name = "currency",
                    column = @Column(name = "price_currency")
            )
    })
    private Money price;
    @Column(nullable = false)
    private Boolean financingEligible;
    @OneToOne
    @JoinColumn(name = "device_payment_agreement_id", unique = true)
    private DevicePaymentAgreement paymentAgreement;

}
