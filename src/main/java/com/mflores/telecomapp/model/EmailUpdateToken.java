package com.mflores.telecomapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "email_update_token")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailUpdateToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_update_token_id", nullable = false)
    private Long emailUpdateTokenId;
    @Column(name = "hashed_token", nullable = false, length = 255)
    private String hashedToken;
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "email_update_status",  nullable = false)
    private EmailUpdateStatus emailUpdateStatus;
    @Column(name = "new_email", nullable = false, length = 255)
    private String newEmail;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_account_id",  nullable = false)
    private UserAccount userAccount;

}
