package com.mflores.telecomapp.tests.customerregistration.controller;

import com.mflores.telecomapp.controller.CustomerRegistrationController;
import com.mflores.telecomapp.dto.*;
import com.mflores.telecomapp.model.BillingLanguage;
import com.mflores.telecomapp.service.CustomerRegistrationService;
import com.mflores.telecomapp.validation.PasswordMatchValidator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerRegistrationController.class)
public class CustomerRegistrationControllerTest {

    @MockitoBean
    private CustomerRegistrationService customerRegistrationService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnCorrectCustomerRegistrationResponse() throws Exception {

        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setBillingLanguage(BillingLanguage.ENGLISH);

        CreateCustomerRegistrationRequest createCustomerRegistrationRequest = new CreateCustomerRegistrationRequest();
        createCustomerRegistrationRequest.setFirstName("John");
        createCustomerRegistrationRequest.setLastName("Doe");
        createCustomerRegistrationRequest.setEmail("doejohn10@gmail.com");
        createCustomerRegistrationRequest.setDateOfBirth(LocalDate.of(1980, 1, 1));

        CreateCustomerCredentialRequest createCustomerCredentialRequest = new CreateCustomerCredentialRequest();
        createCustomerCredentialRequest.setPassword("Password123!");
        createCustomerCredentialRequest.setConfirmPassword("Password123!");

        CreateTotalRegistrationRequest createTotalRegistrationRequest = new CreateTotalRegistrationRequest();
        createTotalRegistrationRequest.setCustomer(createCustomerRegistrationRequest);
        createTotalRegistrationRequest.setAccount(createAccountRequest);
        createTotalRegistrationRequest.setCredentials(createCustomerCredentialRequest);

        CustomerRegistrationResponse customerRegistrationResponse = new CustomerRegistrationResponse();
        customerRegistrationResponse.setAccountNumber("12345");
        customerRegistrationResponse.setEmail("doejohn10@gmail.com");
        customerRegistrationResponse.setFirstName("John");
        customerRegistrationResponse.setLastName("Doe");

        String json = objectMapper.writeValueAsString(createTotalRegistrationRequest);

        when(customerRegistrationService.createCustomerRegistration(createTotalRegistrationRequest.getAccount(),
                createTotalRegistrationRequest.getCustomer(), createTotalRegistrationRequest.getCredentials()))
                .thenReturn(customerRegistrationResponse);

        mockMvc.perform(post("/api/customer-registrations").with(user("usertest"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("doejohn10@gmail.com"))
                .andExpect(jsonPath("$.accountNumber").value("12345"));

        verify(customerRegistrationService).createCustomerRegistration(createTotalRegistrationRequest.getAccount(),
                createTotalRegistrationRequest.getCustomer(), createTotalRegistrationRequest.getCredentials());
    }

    @Test
    void shouldReturnFalseWhenPasswordsDoNotMatch() throws Exception {

        CreateCustomerCredentialRequest createCustomerCredentialRequest = new CreateCustomerCredentialRequest();
        createCustomerCredentialRequest.setPassword("Password123!");
        createCustomerCredentialRequest.setConfirmPassword("Password321!");

        PasswordMatchValidator passwordMatchValidator = new PasswordMatchValidator();

        assertFalse(passwordMatchValidator.isValid(createCustomerCredentialRequest, null));
    }

    @Test
    void shouldReturnBadRequestStatusWhenPasswordsDoNotMatch() throws Exception {

        CreateAccountRequest createAccountRequest = new CreateAccountRequest();
        createAccountRequest.setBillingLanguage(BillingLanguage.ENGLISH);

        CreateCustomerRegistrationRequest createCustomerRegistrationRequest = new CreateCustomerRegistrationRequest();
        createCustomerRegistrationRequest.setFirstName("John");
        createCustomerRegistrationRequest.setLastName("Doe");
        createCustomerRegistrationRequest.setEmail("doejohn10@gmail.com");
        createCustomerRegistrationRequest.setDateOfBirth(LocalDate.of(1980, 1, 1));

        CreateCustomerCredentialRequest createCustomerCredentialRequest = new CreateCustomerCredentialRequest();
        createCustomerCredentialRequest.setPassword("Password123!");
        createCustomerCredentialRequest.setConfirmPassword("Password321!");

        CreateTotalRegistrationRequest createTotalRegistrationRequest = new CreateTotalRegistrationRequest();
        createTotalRegistrationRequest.setCustomer(createCustomerRegistrationRequest);
        createTotalRegistrationRequest.setAccount(createAccountRequest);
        createTotalRegistrationRequest.setCredentials(createCustomerCredentialRequest);

        String json = objectMapper.writeValueAsString(createTotalRegistrationRequest);

        mockMvc.perform(post("/api/customer-registrations")
                        .with(user("usertest"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                .andExpect(status().isBadRequest());

        verify(customerRegistrationService, never()).createCustomerRegistration(any(), any(), any());
    }
}
