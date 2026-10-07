package com.mflores.telecomapp.controller;

import com.mflores.telecomapp.dto.CreateTotalRegistrationRequest;
import com.mflores.telecomapp.dto.CustomerRegistrationResponse;
import com.mflores.telecomapp.service.CustomerRegistrationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer-registrations")
public class CustomerRegistrationController {

    private final CustomerRegistrationService customerRegistrationService;

    public CustomerRegistrationController(CustomerRegistrationService customerRegistrationService) {
        this.customerRegistrationService = customerRegistrationService;
    }

    @PostMapping
    public CustomerRegistrationResponse createCustomerRegistration(@Valid @RequestBody CreateTotalRegistrationRequest request){

        return customerRegistrationService.createCustomerRegistration(
                request.getAccount(),
                request.getCustomer(),
                request.getCredentials()
        );

    }
}
