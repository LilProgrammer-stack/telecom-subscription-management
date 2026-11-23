package com.mflores.telecomapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EmailUpdateRequest(

        @Email
        @NotBlank
        String newEmail,
        @NotBlank
        @Size(max = 255)
        String password
) {
}
