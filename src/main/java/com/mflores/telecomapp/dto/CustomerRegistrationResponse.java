package com.mflores.telecomapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegistrationResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String accountNumber;
}
