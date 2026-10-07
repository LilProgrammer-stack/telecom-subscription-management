package com.mflores.telecomapp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateTotalRegistrationRequest {

    @Valid
    @NotNull
    private CreateCustomerRegistrationRequest customer;

    @Valid
    @NotNull
    private CreateCustomerCredentialRequest credentials;

    @Valid
    @NotNull
    private CreateAccountRequest account;
}
