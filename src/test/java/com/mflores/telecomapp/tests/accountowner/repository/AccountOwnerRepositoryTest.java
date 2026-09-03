package com.mflores.telecomapp.tests.accountowner.repository;

import com.mflores.telecomapp.model.AccountOwner;
import com.mflores.telecomapp.repository.AccountOwnerRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountOwnerRepositoryTest {

    @Autowired
    AccountOwnerRepository accountOwnerRepository;

    @Test
    void shouldSaveAccountOwnerSuccessfully(){

        //Arrange
        AccountOwner accountOwner = new AccountOwner(
                "John",
                "Smith",
                "john.smith@example.com",
                LocalDate.of(1995, 4, 20)
        );

        //Act
        AccountOwner owner = accountOwnerRepository.save(accountOwner);

        //Assert

        assertNotNull(owner.getAccountOwnerId());
        assertEquals("John", owner.getFirstName());
        assertEquals("Smith", owner.getLastName());
        assertEquals("john.smith@example.com", owner.getEmail());
        assertEquals(LocalDate.of(1995,4,20),owner.getDateOfBirth());
    }

    @Test
    void shouldGetAllAccountOwnersSuccessfully(){

        AccountOwner accountOwner1 = new AccountOwner(
                "John",
                "Smith",
                "john.smith@example.com",
                LocalDate.of(1995, 4, 20)
        );
        AccountOwner accountOwner2 = new AccountOwner(
                "Maria",
                "Db",
                "maria.db@example.com",
                LocalDate.of(1889, 12, 20)
        );
        AccountOwner accountOwner3 = new AccountOwner(
                "Karen",
                "Hernandez",
                "karen.hernandez@example.com",
                LocalDate.of(2002, 5, 20)
        );


        accountOwnerRepository.save(accountOwner1);
        accountOwnerRepository.save(accountOwner2);
        accountOwnerRepository.save(accountOwner3);

        List<AccountOwner> accountOwners = accountOwnerRepository.findAll();


        assertEquals(3,accountOwners.size());
        assertEquals("John",accountOwners.get(0).getFirstName());
    }

    @Test
    void shouldReturnEmptyWhenIdDoesNotExist(){

        Long id = 10000L;

        Optional<AccountOwner> accountOwner = accountOwnerRepository.findById(id);

        assertTrue(accountOwner.isEmpty());
    }

    @Test
    void shouldDeleteAccountOwnerByIdSuccessfully() {

        AccountOwner accountOwner = new AccountOwner(
                "Karen",
                "Hernandez",
                "karen.hernandez@example.com",
                LocalDate.of(2002, 5, 20)
        );

        AccountOwner savedAccountOwner = accountOwnerRepository.save(accountOwner);

        accountOwnerRepository.deleteById(savedAccountOwner.getAccountOwnerId());

        Optional<AccountOwner> foundAccountOwner = accountOwnerRepository.findById(savedAccountOwner.getAccountOwnerId());

        assertFalse(foundAccountOwner.isPresent());
    }

    @Test
    void shouldNotAllowDuplicateEmail(){
        AccountOwner owner1 = new AccountOwner(
                "John",
                "Smith",
                "john@example.com",
                LocalDate.of(1995, 4, 20)
        );

        AccountOwner owner2 = new AccountOwner(
                "Maria",
                "Garcia",
                "john@example.com",
                LocalDate.of(1988, 9, 14)
        );

        accountOwnerRepository.saveAndFlush(owner1);

        assertThrows(DataIntegrityViolationException.class, ()-> {
            accountOwnerRepository.saveAndFlush(owner2);
        });
    }

    @Test
    void  shouldNotAllowNullInputs(){
        AccountOwner owner = new AccountOwner(
                null,
                "Garcia",
                "john@example.com",
                LocalDate.of(1995, 4, 20)
        );
        assertThrows(DataIntegrityViolationException.class, ()-> accountOwnerRepository.saveAndFlush(owner));
    }

    @Test
    void shouldGenerateCreatedAtWhenSavingAccountOwner(){

        AccountOwner owner = new AccountOwner("Jose", "Flores", "floresjose60@gmail.com",
                LocalDate.of(2002,11,14));

        AccountOwner savedAccountOwner = accountOwnerRepository.saveAndFlush(owner);

        assertNotNull(savedAccountOwner.getCreatedAt());
    }
}
