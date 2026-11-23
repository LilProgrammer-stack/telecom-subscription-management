package com.mflores.telecomapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "customer_credential")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credential_id", nullable = false)
    private Long credentialId;

    @OneToOne
    @JoinColumn(
            name = "account_owner_id",
            nullable = false,
            unique = true
    )
    private AccountOwner accountOwner;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified;

    @Column(name = "registration_edit_token_hash", length = 64)
    private String registrationEditTokenHash;

    @Column(name = "registration_edit_token_expires_at")
    private OffsetDateTime registrationEditTokenExpiresAt;
}
