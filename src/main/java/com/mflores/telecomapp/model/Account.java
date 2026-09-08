package com.mflores.telecomapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "account")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", nullable = false)
    private Long accountId;
    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "billing_language", nullable = false)
    private BillingLanguage billingLanguage;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus accountStatus;

    @Generated
    @Column(name = "creation_date", nullable = false, updatable = false)
    private OffsetDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private AccountOwner accountOwner;

    @OneToMany(mappedBy = "account")
    //"The account field inside BillingCycle is responsible for this relationship."
    //"The relationship is already managed by the account field inside BillingCycle."
    private List<BillingCycle> billingCycles;

    public Account(String accountNumber, BillingLanguage billingLanguage, AccountStatus accountStatus, AccountOwner accountOwner) {
        this.accountNumber = accountNumber;
        this.billingLanguage = billingLanguage;
        this.accountStatus = accountStatus;
        this.accountOwner = accountOwner;
    }

    public Account(String accountNumber, BillingLanguage billingLanguage, AccountStatus accountStatus, OffsetDateTime creationDate, AccountOwner accountOwner) {
        this.accountNumber = accountNumber;
        this.billingLanguage = billingLanguage;
        this.accountStatus = accountStatus;
        this.creationDate = creationDate;
        this.accountOwner = accountOwner;
    }
}
