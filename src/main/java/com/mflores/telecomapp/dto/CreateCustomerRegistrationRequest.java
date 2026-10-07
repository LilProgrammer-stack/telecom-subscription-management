package com.mflores.telecomapp.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerRegistrationRequest {

    @NotBlank(message = "first name is required")
    private String firstName;
    @NotBlank(message = "last name is required")
    private String lastName;
    @NotBlank(message = "email must be specified")
    @Email(message = "The provided email does not match the standard email format johndoe1@anemail.com")
    private String email;
    @NotNull(message = "Date of birth is required")
    @Past(message = "The date of birth cannot be in the future")
    private LocalDate dateOfBirth;
}
