package com.mflores.telecomapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerLoginRequest {

    @Email(message = "Please provide a valid email address.")
    @NotBlank(message = "The email is required.")
    private String email;
    @NotBlank(message = "The password is required")
    private String password;
}
