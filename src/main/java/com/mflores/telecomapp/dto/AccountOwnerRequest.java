package com.mflores.telecomapp.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AccountOwnerRequest {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
}
