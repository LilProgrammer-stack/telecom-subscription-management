package com.mflores.telecomapp.controller;

import com.mflores.telecomapp.dto.CustomerLoginRequest;
import com.mflores.telecomapp.service.CustomerAuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class CustomerAuthenticationController {

    private final CustomerAuthenticationService customerAuthenticationService;

    public CustomerAuthenticationController(CustomerAuthenticationService customerAuthenticationService) {
        this.customerAuthenticationService = customerAuthenticationService;
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<Void> logIn(@Valid @RequestBody CustomerLoginRequest customerLoginRequest,
                                      HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse) {

        customerAuthenticationService.authenticateCustomer(customerLoginRequest, httpServletRequest, httpServletResponse);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/auth/me")
    public ResponseEntity<String> getCurrentCustomer(
            Authentication authentication) {

        return ResponseEntity.ok(authentication.getName());
    }
}
