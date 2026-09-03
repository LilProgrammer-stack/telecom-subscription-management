package com.mflores.telecomapp.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AccountOwnerResponse {

    private Long accountOwnerId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private OffsetDateTime createdAt;
}
