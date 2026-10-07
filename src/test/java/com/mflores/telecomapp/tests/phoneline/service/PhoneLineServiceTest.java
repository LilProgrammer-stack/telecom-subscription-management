package com.mflores.telecomapp.tests.phoneline.service;

import com.mflores.telecomapp.dto.PhoneLineResponse;
import com.mflores.telecomapp.model.*;
import com.mflores.telecomapp.repository.PhoneLineRepository;
import com.mflores.telecomapp.service.PhoneLineService;
import com.mflores.telecomapp.service.PhoneNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PhoneLineServiceTest {

    @Mock
    private PhoneLineRepository phoneLineRepository;
    @Mock
    private PhoneNumberGenerator phoneNumberGenerator;
    @InjectMocks
    private PhoneLineService phoneLineService;

    private AccountOwner accountOwner = new AccountOwner();
    private Account account = new Account();

    @BeforeEach
    public void setUp() {
        accountOwner.setFirstName("John");
        accountOwner.setLastName("Doe");
        accountOwner.setDateOfBirth(LocalDate.of(2002, 11, 14));
        accountOwner.setEmail("doejohn51@gmail.com");
        accountOwner.setCreatedAt(OffsetDateTime.now());

        account.setAccountOwner(accountOwner);
        account.setCreationDate(accountOwner.getCreatedAt());
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setAccountNumber("TB234567");
        account.setBillingLanguage(BillingLanguage.ENGLISH);
    }

    @Test
    void shouldCreatePhoneLineWithExpectedProperties() {

        /*
        ArgumentCaptor tells you what the service sent to the repository.
        The mocked thenReturn() tells you what the repository gave back to the service.
         */
        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setPhoneLineId(1313133L);
        phoneLine.setAccount(account);
        phoneLine.setPhoneLineStatus(PhoneLineStatus.ACTIVE);
        phoneLine.setPhoneNumber("2125123456");

        when(phoneNumberGenerator.generatePhoneNumber()).thenReturn("2125123456");
        when(phoneLineRepository.save(any(PhoneLine.class))).thenReturn(phoneLine);

        phoneLineService.createPhoneLine(account);

        ArgumentCaptor<PhoneLine> phoneLineArgumentCaptor = ArgumentCaptor.forClass(PhoneLine.class);

        verify(phoneLineRepository).save(phoneLineArgumentCaptor.capture());
        verify(phoneNumberGenerator).generatePhoneNumber();

        PhoneLine savedPhoneLine = phoneLineArgumentCaptor.getValue();

        assertEquals(account, savedPhoneLine.getAccount());
        //Your test should instead verify the properties the service is responsible for setting:
        //assertEquals(1313133L, savedPhoneLine.getPhoneLineId());
        assertEquals(PhoneLineStatus.ACTIVE, savedPhoneLine.getPhoneLineStatus());
        assertEquals("2125123456", savedPhoneLine.getPhoneNumber());
    }

    @Test
    void shouldReturnPhoneLineResponse() {

        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setPhoneLineId(1313133L);
        phoneLine.setAccount(account);
        phoneLine.setPhoneLineStatus(PhoneLineStatus.ACTIVE);
        phoneLine.setPhoneNumber("2125123456");

        when(phoneNumberGenerator.generatePhoneNumber()).thenReturn("2125123456");
        when(phoneLineRepository.save(any(PhoneLine.class))).thenReturn(phoneLine);

        PhoneLineResponse phoneLineResponse = phoneLineService.createPhoneLine(account);

        assertEquals(1313133L, phoneLineResponse.getPhoneLineId());
        assertEquals(PhoneLineStatus.ACTIVE, phoneLineResponse.getPhoneLineStatus());
        assertEquals("2125123456", phoneLineResponse.getPhoneNumber());

        verify(phoneLineRepository).save(any(PhoneLine.class));
        verify(phoneNumberGenerator).generatePhoneNumber();
    }

    @Test
    void shouldThrowExceptionWhenSuspendingPhoneLineThatHasBeenSuspendedAlready() {

        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setAccount(account);
        phoneLine.setPhoneNumber(phoneNumberGenerator.generatePhoneNumber());
        phoneLine.setPhoneLineStatus(PhoneLineStatus.SUSPENDED);

        assertThrows(IllegalArgumentException.class, () -> phoneLineService.suspendPhoneLine(phoneLine));

        verify(phoneLineRepository, never()).save(any(PhoneLine.class));
    }

    @Test
    void shouldThrowExceptionWhenSuspendingPhoneLineThatHasBeenTerminated() {

        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setAccount(account);
        phoneLine.setPhoneLineStatus(PhoneLineStatus.TERMINATED);

        assertThrows(IllegalArgumentException.class, () -> phoneLineService.suspendPhoneLine(phoneLine));

        verify(phoneLineRepository, never()).save(any(PhoneLine.class));
    }

    @Test
    void shouldSuspendLineWhenPhoneNumberIsActive() {

        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setAccount(account);
        phoneLine.setPhoneLineStatus(PhoneLineStatus.ACTIVE);

        when(phoneLineRepository.save(any(PhoneLine.class))).thenReturn(phoneLine);

        PhoneLineResponse phoneLineResponse = phoneLineService.suspendPhoneLine(phoneLine);

        assertEquals(PhoneLineStatus.SUSPENDED, phoneLineResponse.getPhoneLineStatus());

        ArgumentCaptor<PhoneLine> phoneLineArgumentCaptor = ArgumentCaptor.forClass(PhoneLine.class);

        verify(phoneLineRepository).save(phoneLineArgumentCaptor.capture());
        verify(phoneNumberGenerator, never()).generatePhoneNumber();

        PhoneLine savedPhoneLine = phoneLineArgumentCaptor.getValue();
        assertEquals(account, savedPhoneLine.getAccount());
        assertEquals(PhoneLineStatus.SUSPENDED, savedPhoneLine.getPhoneLineStatus());

    }

    @Test
    void shouldThrowExceptionWhenReactivatingPhoneLineThatHasBeenTerminated() {

        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setAccount(account);
        phoneLine.setPhoneLineStatus(PhoneLineStatus.TERMINATED);

        assertThrows(IllegalArgumentException.class, () -> phoneLineService.reactivatePhoneLine(phoneLine));

        verify(phoneLineRepository, never()).save(any(PhoneLine.class));
    }

    @Test
    void shouldThrowExceptionWhenReactivatingPhoneLineThatHasBeenActivatedAlready() {

        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setAccount(account);
        phoneLine.setPhoneLineStatus(PhoneLineStatus.ACTIVE);

        assertThrows(IllegalArgumentException.class, () -> phoneLineService.reactivatePhoneLine(phoneLine));

        verify(phoneLineRepository, never()).save(any(PhoneLine.class));
    }

    @Test
    void shouldReactivatePhoneLineWhenSuspended() {

        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setAccount(account);
        phoneLine.setPhoneLineStatus(PhoneLineStatus.SUSPENDED);

        when(phoneLineRepository.save(any(PhoneLine.class))).thenReturn(phoneLine);

        PhoneLineResponse phoneLineResponse = phoneLineService.reactivatePhoneLine(phoneLine);

        assertEquals(PhoneLineStatus.ACTIVE, phoneLineResponse.getPhoneLineStatus());

        ArgumentCaptor<PhoneLine> phoneLineArgumentCaptor = ArgumentCaptor.forClass(PhoneLine.class);

        verify(phoneLineRepository).save(phoneLineArgumentCaptor.capture());

        PhoneLine savedPhoneLine = phoneLineArgumentCaptor.getValue();

        assertEquals(account, savedPhoneLine.getAccount());
        assertEquals(PhoneLineStatus.ACTIVE, savedPhoneLine.getPhoneLineStatus());

        verify(phoneNumberGenerator,  never()).generatePhoneNumber();
    }

    @Test
    void shouldNotAllowToTerminatePhoneLineWhenItIsAlreadyTerminated() {
        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setAccount(account);
        phoneLine.setPhoneLineStatus(PhoneLineStatus.TERMINATED);

        assertThrows(IllegalArgumentException.class, () -> phoneLineService.terminatePhoneLine(phoneLine));

        verify(phoneLineRepository, never()).save(any(PhoneLine.class));
        verify(phoneNumberGenerator, never()).generatePhoneNumber();
    }

    @Test
    void shouldTerminatePhoneLineWhenActive() {
        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setAccount(account);
        phoneLine.setPhoneLineStatus(PhoneLineStatus.ACTIVE);

        when(phoneLineRepository.save(any(PhoneLine.class))).thenReturn(phoneLine);

        PhoneLineResponse phoneLineResponse = phoneLineService.terminatePhoneLine(phoneLine);

        ArgumentCaptor<PhoneLine> phoneLineArgumentCaptor = ArgumentCaptor.forClass(PhoneLine.class);

        verify(phoneLineRepository).save(phoneLineArgumentCaptor.capture());

        PhoneLine savedPhoneLine = phoneLineArgumentCaptor.getValue();

        assertEquals(account, savedPhoneLine.getAccount());
        assertEquals(PhoneLineStatus.TERMINATED, savedPhoneLine.getPhoneLineStatus());

        assertEquals(PhoneLineStatus.TERMINATED, phoneLineResponse.getPhoneLineStatus());

        verify(phoneNumberGenerator, never()).generatePhoneNumber();
    }

    @Test
    void shouldTerminatePhoneLineWhenSuspended() {
        PhoneLine phoneLine = new PhoneLine();
        phoneLine.setAccount(account);
        phoneLine.setPhoneLineStatus(PhoneLineStatus.SUSPENDED);

        when(phoneLineRepository.save(any(PhoneLine.class))).thenReturn(phoneLine);

        PhoneLineResponse phoneLineResponse = phoneLineService.terminatePhoneLine(phoneLine);

        ArgumentCaptor<PhoneLine> phoneLineArgumentCaptor = ArgumentCaptor.forClass(PhoneLine.class);

        verify(phoneLineRepository).save(phoneLineArgumentCaptor.capture());

        PhoneLine savedPhoneLine = phoneLineArgumentCaptor.getValue();

        assertEquals(account, savedPhoneLine.getAccount());
        assertEquals(PhoneLineStatus.TERMINATED, savedPhoneLine.getPhoneLineStatus());

        assertEquals(PhoneLineStatus.TERMINATED, phoneLineResponse.getPhoneLineStatus());

        verify(phoneNumberGenerator, never()).generatePhoneNumber();
    }
}
