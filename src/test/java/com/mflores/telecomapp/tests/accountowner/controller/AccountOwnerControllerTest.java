package com.mflores.telecomapp.tests.accountowner.controller;


import com.mflores.telecomapp.controller.AccountOwnerController;
import com.mflores.telecomapp.dto.AccountOwnerRequest;
import com.mflores.telecomapp.dto.AccountOwnerResponse;
import com.mflores.telecomapp.exception.ResourceNotFoundException;
import com.mflores.telecomapp.service.AccountOwnerService;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@WebMvcTest(AccountOwnerController.class)
public class AccountOwnerControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountOwnerService accountOwnerService;

    private static OffsetDateTime createdAt;

    @BeforeAll()
    static void setUp(){
        createdAt = OffsetDateTime.of(
                2026, 8, 31,
                12, 30, 0, 0,
                ZoneOffset.of("-06:00")
        );
    }

    /*
    Think of it as a fake HTTP client inside your test.
    Instead of opening Postman
    we can do it programmatically

    You need to send JSON:

    .content(...)

    and specify that the content is JSON:

    .contentType(MediaType.APPLICATION_JSON)
     */


    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldGetAccountOwnerByIdSuccessfully() throws Exception {

        //Arrange
        Long id = 1L;

        AccountOwnerResponse accountOwnerResponse = new AccountOwnerResponse(
                1L,
                "John",
                "Smith",
                "john.smith@example.com",
                LocalDate.of(1995, 4, 20),
                createdAt);

        when(accountOwnerService.getAccountOwnerById(id))
                .thenReturn(accountOwnerResponse);

        //Act

        mockMvc.perform(get("/api/accountOwners/1").with(user("testuser")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountOwnerId").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.email").value("john.smith@example.com"))
                .andExpect(jsonPath("$.dateOfBirth").value("1995-04-20"));

        verify(accountOwnerService).getAccountOwnerById(id);
    }

    @Test
    void shouldCreateAccountOwnerSuccessfully() throws Exception {

/*
        You need to send JSON:
        .content(...)
        and specify that the content is JSON:
        .contentType(MediaType.APPLICATION_JSON)
*/

        //Arrange
        AccountOwnerRequest accountOwnerRequest = new AccountOwnerRequest(
                "John",
                "Smith",
                "john.smith@example.com",
                LocalDate.of(1995, 4, 20));

        AccountOwnerResponse accountOwnerResponse = new AccountOwnerResponse(
                1L,
                "John",
                "Smith",
                "john.smith@example.com",
                LocalDate.of(1995, 4, 20),
                createdAt);

        when(accountOwnerService.createAccountOwner(any(AccountOwnerRequest.class)))
                .thenReturn(accountOwnerResponse);

        String json = objectMapper.writeValueAsString(accountOwnerRequest);

        //Act and Assert
        mockMvc.perform(post("/api/accountOwners").with(user("testuser"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountOwnerId").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john.smith@example.com"))
                .andExpect(jsonPath("$.dateOfBirth").value("1995-04-20"));

        ArgumentCaptor<AccountOwnerRequest> argumentCaptor = ArgumentCaptor.forClass(AccountOwnerRequest.class);

        verify(accountOwnerService).createAccountOwner(argumentCaptor.capture());

        AccountOwnerRequest captor = argumentCaptor.getValue();

        assertEquals("John", captor.getFirstName());
        assertEquals("Smith", captor.getLastName());
        assertEquals("john.smith@example.com", captor.getEmail());
        assertEquals(LocalDate.of(1995, 4, 20), captor.getDateOfBirth());
    }

    @Test
    void shouldNotCreateAccountOwnerWhenRequestIsNotValid() throws Exception {

        //Arrange
        AccountOwnerRequest accountOwnerRequest = new AccountOwnerRequest(
                " ",
                " ",
                "john.smithexample.com",
                LocalDate.of(2030, 4, 20));

        String json = objectMapper.writeValueAsString(accountOwnerRequest);

        mockMvc.perform(post("/api/accountOwners").with(user("testuser")).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON).content(json))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.firstName").value("A first name is required"))
                .andExpect(jsonPath("$.errors.lastName").value("A last name is required"))
                .andExpect(jsonPath("$.errors.email").value("Please provide a valid email address"))
                .andExpect(jsonPath("$.errors.dateOfBirth").value("Date of birth must be in the past"));

        verify(accountOwnerService, never()).createAccountOwner(any(AccountOwnerRequest.class));
    }

    @Test
    void shouldGetAllAccountOwnersSuccessfully() throws Exception{

        //Arrange
        AccountOwnerResponse accountOwner1 = new AccountOwnerResponse(
                1L,
                "John",
                "Smith",
                "john.smith@example.com",
                LocalDate.of(1995, 4, 20),
                createdAt
        );

        AccountOwnerResponse accountOwner2 = new AccountOwnerResponse(
                2L,
                "Maria",
                "Garcia",
                "maria.garcia@example.com",
                LocalDate.of(1988, 9, 14),
                createdAt
        );

        AccountOwnerResponse accountOwner3 = new AccountOwnerResponse(
                3L,
                "David",
                "Johnson",
                "david.johnson@example.com",
                LocalDate.of(2001, 12, 3),
                createdAt
        );

        when(accountOwnerService.getAllAccountOwners())
                .thenReturn(List.of(accountOwner1,accountOwner2,accountOwner3));

        mockMvc.perform(get("/api/accountOwners").with(user("testuser")).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].accountOwnerId").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Maria"))
                .andExpect(jsonPath("$[2].firstName").value("David"))
                .andExpect(jsonPath("$[0].dateOfBirth").value("1995-04-20"));

        verify(accountOwnerService).getAllAccountOwners();
    }


    @Test
    void shouldUpdateAccountOwnerSuccessfullyById() throws Exception{

        //Arrange
        Long id = 1L;
        AccountOwnerRequest accountOwnerRequest = new AccountOwnerRequest("Miguel", "Flores",
                "floresjosex50@gmail.com", LocalDate.of(2002,11, 14));
        AccountOwnerResponse accountOwnerResponse = new AccountOwnerResponse(id, "Miguel", "Flores", "floresjosex50@gmail.com",
                LocalDate.of(2002, 11, 14),
                createdAt);

        when(accountOwnerService.updateAccountOwnerById(id,accountOwnerRequest))
                .thenReturn(accountOwnerResponse);

        String json = objectMapper.writeValueAsString(accountOwnerRequest);
        //Act

        //Assert
        mockMvc.perform(put("/api/accountOwners/{id}",id)
                        .with(user("testuser"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountOwnerId").value(1L))
                .andExpect(jsonPath("$.firstName").value("Miguel"))
                .andExpect(jsonPath("$.lastName").value("Flores"))
                .andExpect(jsonPath("$.email").value("floresjosex50@gmail.com"))
                .andExpect(jsonPath("$.dateOfBirth").value("2002-11-14"));

        ArgumentCaptor<AccountOwnerRequest> accountOwnerRequestArgumentCaptor = ArgumentCaptor.forClass(AccountOwnerRequest.class);


        verify(accountOwnerService).updateAccountOwnerById(eq(id), accountOwnerRequestArgumentCaptor.capture());

        AccountOwnerRequest ownerRequest = accountOwnerRequestArgumentCaptor.getValue();

        assertEquals("Miguel", ownerRequest.getFirstName());
        assertEquals("Flores", ownerRequest.getLastName());
        assertEquals("floresjosex50@gmail.com", ownerRequest.getEmail());
        assertEquals(LocalDate.of(2002,11,14), ownerRequest.getDateOfBirth());

    }

    @Test
    void shouldDeleteAccountOwnerByIdSuccessfully() throws Exception{

        //Arrange

        Long id = 1L;

        mockMvc.perform(delete("/api/accountOwners/{id}",id)
                .with(user("testuser"))
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(accountOwnerService).deleteAccountOwnerById(id);
    }

    @Test
    void shouldNotDeleteAccountOwnerIfIdIsNotFound() throws Exception{

        Long id = 10000L;

        doThrow(new ResourceNotFoundException("The account owner id " + id + " was not found"))
                .when(accountOwnerService)
                        .deleteAccountOwnerById(id);

        mockMvc.perform(delete("/api/accountOwners/{id}",id)
                .with(user("testuser"))
                .with(csrf()))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.dateTime").exists())
                .andExpect(jsonPath("$.error").value("Resource Not Found"))
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.status").value(404));

        verify(accountOwnerService).deleteAccountOwnerById(id);
    }

    @Test
    void shouldNotUpdateAccountOwnerIfIdIsNotFound() throws Exception{

        Long id = 10000L;
        AccountOwnerRequest accountOwnerRequest = new AccountOwnerRequest("Miguel", "Flores",
                "floresjosex50@gmail.com", LocalDate.of(2002,11, 14));

        when(accountOwnerService.updateAccountOwnerById(eq(id), any(AccountOwnerRequest.class)))
                .thenThrow(new ResourceNotFoundException("The account owner id " + id + " was not found"));

        String accountOwnerRequestJson = objectMapper.writeValueAsString(accountOwnerRequest);

        mockMvc.perform(put("/api/accountOwners/{id}",id)
                .with(user("usertest"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountOwnerRequestJson))

                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.dateTime").exists())
                .andExpect(jsonPath("$.error").value("Resource Not Found"))
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.status").value(404));

        ArgumentCaptor<AccountOwnerRequest> argumentCaptor = ArgumentCaptor.forClass(AccountOwnerRequest.class);

        //AccountOwnerRequest ownerRequest = argumentCaptor.capture();

        verify(accountOwnerService).updateAccountOwnerById(eq(id), argumentCaptor.capture());

    }

    @Test
    void shouldNotCallUpdateByIdWhenRequestIsInvalid() throws Exception{

        Long id = 1L;

        AccountOwnerRequest accountOwnerRequest = new AccountOwnerRequest(" ", " ",
                "notandemailgmail.com", LocalDate.of(2672,11, 14));

        String accountOwnerRequestJson = objectMapper.writeValueAsString(accountOwnerRequest);

        mockMvc.perform(put("/api/accountOwners/{id}",id)
                .with(user("usertest"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountOwnerRequestJson))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dateTime").exists())
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.errors.lastName").value("A last name is required"))
                .andExpect(jsonPath("$.errors.firstName").value("A first name is required"))
                .andExpect(jsonPath("$.errors.dateOfBirth").value("Date of birth must be in the past"))
                .andExpect(jsonPath("$.errors.email").value("Please provide a valid email address"))
                .andExpect(jsonPath("$.status").value(400));


        verifyNoInteractions(accountOwnerService);



    }

}
