package com.mflores.telecomapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_account_id", nullable = false)
    private Long userAccountId;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;
    @Column(name = "email_verified",  nullable = false)
    private boolean emailVerified;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="role_id",  nullable = false)
    private Role role;
}
