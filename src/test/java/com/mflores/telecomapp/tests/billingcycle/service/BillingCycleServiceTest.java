package com.mflores.telecomapp.tests.billingcycle.service;

import com.mflores.telecomapp.dto.BillingCycleResponse;
import com.mflores.telecomapp.model.Account;
import com.mflores.telecomapp.model.BillingCycle;
import com.mflores.telecomapp.repository.BillingCycleRepository;
import com.mflores.telecomapp.service.BillingCycleService;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BillingCycleServiceTest {

    @Mock
    private BillingCycleRepository billingCycleRepository;

    @InjectMocks
    private BillingCycleService billingCycleService;

    @Test
    void shouldCreateFirstBillingCycle() {

        Account account = new Account();
        account.setCreationDate(OffsetDateTime.of(
                LocalDate.of(2002, 10, 12),
                LocalTime.of(12, 40),
                ZoneOffset.ofHours(-6)
        ));

        BillingCycle billingCycle = new BillingCycle();
        billingCycle.setAccount(account);
        billingCycle.setCycleNumber(1);
        billingCycle.setPeriodStartDate(LocalDate.of(2002, 10, 12));
        billingCycle.setPeriodEndDate(LocalDate.of(2002, 11, 11));

        when(billingCycleRepository.findFirstByAccountOrderByCycleNumberDesc(any(Account.class)))
                .thenReturn(Optional.empty());

        when(billingCycleRepository.save(any(BillingCycle.class)))
                .thenReturn(billingCycle);

        BillingCycleResponse cycleResponse = billingCycleService.createBillingCycle(account);

        assertEquals(1, cycleResponse.getCycleNumber());
        assertEquals(LocalDate.of(2002, 10, 12), cycleResponse.getPeriodStartDate());
        assertEquals(LocalDate.of(2002, 11, 11), cycleResponse.getPeriodEndDate());

        verify(billingCycleRepository).findFirstByAccountOrderByCycleNumberDesc(account);
        verify(billingCycleRepository).save(any(BillingCycle.class));
    }

    @Test
    void shouldCreateNextBillingCycle() {

        Account account = new Account();

        BillingCycle cycle2 = new BillingCycle(
                2,
                LocalDate.of(2026, 2, 15),
                LocalDate.of(2026, 3, 14),
                account
        );

        BillingCycle cycle3 = new BillingCycle();
        cycle3.setCycleNumber(3);
        cycle3.setPeriodStartDate(LocalDate.of(2026, 3, 15));
        cycle3.setPeriodEndDate(LocalDate.of(2026, 4, 14));
        cycle3.setAccount(account);

        when(billingCycleRepository.findFirstByAccountOrderByCycleNumberDesc(any(Account.class)))
        .thenReturn(Optional.of(cycle2));

        when(billingCycleRepository.save(any(BillingCycle.class)))
                .thenReturn(cycle3);

        BillingCycleResponse savedCycle = billingCycleService.createBillingCycle(account);

        assertEquals(3, savedCycle.getCycleNumber());
        assertEquals(LocalDate.of(2026, 3, 15), savedCycle.getPeriodStartDate());
        assertEquals(LocalDate.of(2026, 4, 14), savedCycle.getPeriodEndDate());

        verify(billingCycleRepository).findFirstByAccountOrderByCycleNumberDesc(account);


        ArgumentCaptor<BillingCycle> captor = ArgumentCaptor.forClass(BillingCycle.class);
        verify(billingCycleRepository).save(captor.capture());
        BillingCycle capturedCycle = captor.getValue();
        assertEquals(3, capturedCycle.getCycleNumber());
        assertEquals(LocalDate.of(2026, 3, 15), capturedCycle.getPeriodStartDate());
        assertEquals(LocalDate.of(2026, 4, 14), capturedCycle.getPeriodEndDate());

    }
}
