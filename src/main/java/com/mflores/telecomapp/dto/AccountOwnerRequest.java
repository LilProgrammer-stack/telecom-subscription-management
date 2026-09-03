package com.mflores.telecomapp.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AccountOwnerRequest {
    @NotBlank(message = "A first name is required")
    private String firstName;
    @NotBlank(message = "A last name is required")
    private String lastName;
    @NotBlank(message = "An emails is required")
    @Email(message = "Please provide a valid email address")
    private String email;
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
}
