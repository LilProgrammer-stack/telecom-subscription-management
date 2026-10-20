package com.mflores.telecomapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "device_payment_agreement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevicePaymentAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long devicePaymentAgreementId;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column
    private LocalDate endDate;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "amountInCents",
                    column = @Column(name = "financed_amount")
            ),
            @AttributeOverride(
                    name = "currency",
                    column = @Column(name = "financed_currency")
            )
    })
    private Money financedAmount;
    @Column(nullable = false)
    private Integer totalInstallments;
    @Column(nullable = false)
    private Integer paidInstallments;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

}
