package com.mflores.telecomapp.tests.accountowner.service;

import com.mflores.telecomapp.dto.AccountOwnerRequest;
import com.mflores.telecomapp.dto.AccountOwnerResponse;
import com.mflores.telecomapp.exception.ResourceNotFoundException;
import com.mflores.telecomapp.model.AccountOwner;
import com.mflores.telecomapp.repository.AccountOwnerRepository;
import com.mflores.telecomapp.service.AccountOwnerService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AccountOwnerServiceTest {


    @Mock
    private AccountOwnerRepository accountOwnerRepository;

    @InjectMocks
    private AccountOwnerService accountOwnerService;

    private static OffsetDateTime createdAt;

    @BeforeAll
    static void setUp(){
        createdAt = OffsetDateTime.of(
                2026, 8, 31,
                12, 30, 0, 0,
                ZoneOffset.of("-06:00")
        );
    }

    @Test
    void shouldReturnEmptyListWhenNoAccountOwnerHasBeenCreated(){

        //Arrange
        when(accountOwnerRepository.findAll())
                .thenReturn(Collections.emptyList());

        //Act
        List<AccountOwnerResponse> accountOwnerResponses = accountOwnerService.getAllAccountOwners();

        //Assert
        assertTrue(accountOwnerResponses.isEmpty());

        verify(accountOwnerRepository).findAll();
        verifyNoMoreInteractions(accountOwnerRepository);

    }

    @Test
    void shouldGetAllAccountOwnersSuccessfully(){

        //Arrange
        AccountOwner accountOwner1 = new AccountOwner(
                "John",
                "Smith",
                "john.smith@example.com",
                LocalDate.of(1995, 4, 20)
        );

        AccountOwner accountOwner2 = new AccountOwner(
                "Maria",
                "Garcia",
                "maria.garcia@example.com",
                LocalDate.of(1988, 9, 14)
        );


        AccountOwner accountOwner3 = new AccountOwner(
                "David",
                "Johnson",
                "david.johnson@example.com",
                LocalDate.of(2001, 12, 3)
        );

        when(accountOwnerRepository.findAll())
                .thenReturn(List.of(accountOwner1,accountOwner2,accountOwner3));

        //Act
        List<AccountOwnerResponse> accountOwners = accountOwnerService.getAllAccountOwners();

        //Assert
        assertEquals(3, accountOwners.size());
        assertEquals("Maria", accountOwners.get(1).getFirstName());
        assertEquals("Garcia", accountOwners.get(1).getLastName());
        assertEquals("maria.garcia@example.com", accountOwners.get(1).getEmail());
        assertEquals(LocalDate.of(1988, 9, 14), accountOwners.get((1)).getDateOfBirth());
        verify(accountOwnerRepository).findAll();
    }

    @Test
    void shouldCreateAccountOwnerSuccessfully(){

        //Arrange
        AccountOwnerRequest accountOwnerRequest = new AccountOwnerRequest(
                "John",
                "Smith",
                "john.smith@example.com",
                LocalDate.of(1995, 4, 20)
        );

        AccountOwner accountOwner = new AccountOwner(
                "John",
                "Smith",
                "john.smith@example.com",
                LocalDate.of(1995, 4, 20)
        );

        when(accountOwnerRepository.save(any(AccountOwner.class)))
                .thenReturn(accountOwner);

        ArgumentCaptor<AccountOwner> argumentCaptor = ArgumentCaptor.forClass(AccountOwner.class);

        //Act
        AccountOwnerResponse accountOwnerResponse = accountOwnerService.createAccountOwner(accountOwnerRequest);

        // Assert
        assertEquals("John", accountOwnerResponse.getFirstName());
        assertEquals("Smith", accountOwnerResponse.getLastName());
        assertEquals("john.smith@example.com", accountOwnerResponse.getEmail());
        assertEquals(
                LocalDate.of(1995, 4, 20),
                accountOwnerResponse.getDateOfBirth()
        );

        verify(accountOwnerRepository)
                .save(argumentCaptor.capture());

        AccountOwner capturedAccountOwner =
                argumentCaptor.getValue();

        assertEquals("John", capturedAccountOwner.getFirstName());
        assertEquals("Smith", capturedAccountOwner.getLastName());
        assertEquals("john.smith@example.com", capturedAccountOwner.getEmail());
        assertEquals(
                LocalDate.of(1995, 4, 20),
                capturedAccountOwner.getDateOfBirth()
        );

    }

    @Test
    void shouldUpdateAccountOwnerSuccessfully(){

        //Arrange
        Long id = 1L;

        AccountOwnerRequest accountOwnerRequest = new AccountOwnerRequest(
                "Miguel",
                "Flores",
                "miguel.flores@example.com",
                LocalDate.of(1995, 4, 20)
        );

        AccountOwner accountOwner = new AccountOwner(
                "John",
                "Smith",
                "john.smith@example.com",
                LocalDate.of(1995, 4, 20)
        );
        accountOwner.setAccountOwnerId(id);

        when(accountOwnerRepository.findById(id))
                .thenReturn(Optional.of(accountOwner));

        when(accountOwnerRepository.save(accountOwner))
                .thenReturn(accountOwner);

        //Act
        AccountOwnerResponse accountOwnerResponse = accountOwnerService.updateAccountOwnerById(id, accountOwnerRequest);

        //Assert
        assertEquals(id, accountOwnerResponse.getAccountOwnerId());
        assertEquals("Miguel", accountOwnerResponse.getFirstName());
        assertEquals("Flores", accountOwnerResponse.getLastName());
        assertEquals("miguel.flores@example.com", accountOwnerResponse.getEmail());
        assertEquals( LocalDate.of(1995, 4, 20), accountOwnerResponse.getDateOfBirth());

        verify(accountOwnerRepository).save(accountOwner);
        verify(accountOwnerRepository).findById(id);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenAccountOwnerDoesNotExist(){

        Long id = 1L;

        when(accountOwnerRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()-> accountOwnerService.getAccountOwnerById(id));

        verify(accountOwnerRepository).findById(id);
    }

    @Test
    void shouldDeleteAccountOwnerById(){

        Long id = 1L;
        AccountOwner accountOwner = new AccountOwner(
                1L,
                "Miguel",
                "Flores",
                "miguel.flores@example.com",
                LocalDate.of(1995, 4, 20),
                createdAt
        );

        when(accountOwnerRepository.findById(id))
                .thenReturn(Optional.of(accountOwner));

        accountOwnerService.deleteAccountOwnerById(id);

        verify(accountOwnerRepository).findById(id);
        verify(accountOwnerRepository).delete(accountOwner);
    }

    @Test
    void shouldReturnAccountOwnerById(){

        Long id = 1L;

        AccountOwner accountOwner = new AccountOwner(
                1L,
                "Miguel",
                "Flores",
                "miguel.flores@example.com",
                LocalDate.of(1995, 4, 20),
                createdAt
        );

        when(accountOwnerRepository.findById(id))
                .thenReturn(Optional.of(accountOwner));

        AccountOwnerResponse owner = accountOwnerService.getAccountOwnerById(id);

        assertEquals("Miguel", owner.getFirstName());
        assertEquals("Flores", owner.getLastName());
        assertEquals("miguel.flores@example.com", owner.getEmail());
        assertEquals(1L, owner.getAccountOwnerId());

        verify(accountOwnerRepository).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenAccountOwnerNotFoundWhileUpdating(){

        Long id = 1L;

        AccountOwnerRequest accountOwner = new AccountOwnerRequest(
                "Miguel",
                "Flores",
                "miguel.flores@example.com",
                LocalDate.of(1995, 4, 20)
        );

        when(accountOwnerRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()->accountOwnerService.updateAccountOwnerById(id, accountOwner));

        verify(accountOwnerRepository).findById(id);
        verify(accountOwnerRepository, never()).save(any(AccountOwner.class));
    }

    @Test
    void shouldThrowExceptionWhenAccountOwnerNotFoundWhileDeleting(){

        Long id = 1L;

        when(accountOwnerRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()-> accountOwnerService.deleteAccountOwnerById(id));

        verify(accountOwnerRepository).findById(id);
        verify(accountOwnerRepository, never()).delete(any(AccountOwner.class));
    }

}

