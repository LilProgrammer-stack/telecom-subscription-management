package com.mflores.telecomapp.tests.devicepaymentagreement.repository;

import com.mflores.telecomapp.model.*;
import com.mflores.telecomapp.repository.AccountOwnerRepository;
import com.mflores.telecomapp.repository.AccountRepository;
import com.mflores.telecomapp.repository.DevicePaymentAgreementRepository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class DevicePaymentAgreementRepositoryTest {

    @Autowired
    private AccountOwnerRepository accountOwnerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private DevicePaymentAgreementRepository devicePaymentAgreementRepository;

    private AccountOwner accountOwner;
    private Account account;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setup() {
        accountOwner = new AccountOwner("Miguel", "Flores", "floresjosex50@gmail.com",
                LocalDate.of(2002, 11, 12));
        accountOwnerRepository.save(accountOwner);

        account = new Account("123", BillingLanguage.ENGLISH, AccountStatus.ACTIVE, OffsetDateTime.now(),
                accountOwner);
        accountRepository.save(account);

        startDate = LocalDate.of(2002, 11, 12);
        endDate = startDate.plusYears(3);
    }


    @Test
    void shouldReturnDevicePaymentAgreementByAccount() {

        DevicePaymentAgreement devicePaymentAgreement = new DevicePaymentAgreement();
        devicePaymentAgreement.setStartDate(startDate);
        devicePaymentAgreement.setEndDate(endDate);
        devicePaymentAgreement.setTotalInstallments(36);
        devicePaymentAgreement.setPaidInstallments(6);
        Money financedAmount = new Money(100000, Currency.getInstance("USD"));
        devicePaymentAgreement.setFinancedAmount(financedAmount);
        devicePaymentAgreement.setAccount(account);

        devicePaymentAgreementRepository.saveAndFlush(devicePaymentAgreement);


        DevicePaymentAgreement agreement1 = new DevicePaymentAgreement();
        agreement1.setStartDate(startDate);
        agreement1.setEndDate(endDate);
        agreement1.setTotalInstallments(36);
        agreement1.setPaidInstallments(6);
        Money financedAmount1 = new Money(100000, Currency.getInstance("USD"));
        agreement1.setFinancedAmount(financedAmount1);
        agreement1.setAccount(account);

        devicePaymentAgreementRepository.saveAndFlush(agreement1);


        DevicePaymentAgreement agreement2 = new DevicePaymentAgreement();
        agreement2.setStartDate(startDate);
        agreement2.setEndDate(endDate);
        agreement2.setTotalInstallments(24);
        agreement2.setPaidInstallments(12);
        Money financedAmount2 = new Money(45000, Currency.getInstance("USD"));
        agreement2.setFinancedAmount(financedAmount2);
        agreement2.setAccount(account);

        devicePaymentAgreementRepository.saveAndFlush(agreement2);

        List<DevicePaymentAgreement> devicePaymentAgreements = devicePaymentAgreementRepository.findByAccount(account);

        assertEquals(3, devicePaymentAgreements.size());

    }

    @Test
    void shouldFilterBasedOnTheProvidedAccount() {

        AccountOwner accountOwner2 = new AccountOwner("Sofia", "Ramirez", "sofia.ramirez.dev@gmail.com",
                LocalDate.of(1995, 8, 24));
        accountOwnerRepository.save(accountOwner2);

        Account account2 = new Account("456", BillingLanguage.SPANISH, AccountStatus.ACTIVE, OffsetDateTime.now(),
                accountOwner2);
        accountRepository.save(account2);

        DevicePaymentAgreement sofiaDpa1 = new DevicePaymentAgreement();
        sofiaDpa1.setStartDate(startDate);
        sofiaDpa1.setEndDate(endDate);
        sofiaDpa1.setTotalInstallments(24);
        sofiaDpa1.setPaidInstallments(0);
        Money financedAmountSofia1 = new Money(120000, Currency.getInstance("USD")); // $1,200.00
        sofiaDpa1.setFinancedAmount(financedAmountSofia1);
        sofiaDpa1.setAccount(account2);
        devicePaymentAgreementRepository.saveAndFlush(sofiaDpa1);

        DevicePaymentAgreement sofiaDpa2 = new DevicePaymentAgreement();
        LocalDate secondaryStartDate = startDate.plusMonths(1);
        sofiaDpa2.setStartDate(secondaryStartDate);
        sofiaDpa2.setEndDate(secondaryStartDate.plusYears(3));
        sofiaDpa2.setTotalInstallments(36);
        sofiaDpa2.setPaidInstallments(2);
        Money financedAmountSofia2 = new Money(35000, Currency.getInstance("USD"));
        sofiaDpa2.setFinancedAmount(financedAmountSofia2);
        sofiaDpa2.setAccount(account2);
        devicePaymentAgreementRepository.saveAndFlush(sofiaDpa2);


        DevicePaymentAgreement devicePaymentAgreement = new DevicePaymentAgreement();
        devicePaymentAgreement.setStartDate(startDate);
        devicePaymentAgreement.setEndDate(endDate);
        devicePaymentAgreement.setTotalInstallments(36);
        devicePaymentAgreement.setPaidInstallments(6);
        Money financedAmount = new Money(100000, Currency.getInstance("USD"));
        devicePaymentAgreement.setFinancedAmount(financedAmount);
        devicePaymentAgreement.setAccount(account);
        devicePaymentAgreementRepository.saveAndFlush(devicePaymentAgreement);

        DevicePaymentAgreement agreement1 = new DevicePaymentAgreement();
        agreement1.setStartDate(startDate);
        agreement1.setEndDate(endDate);
        agreement1.setTotalInstallments(36);
        agreement1.setPaidInstallments(6);
        Money financedAmount1 = new Money(100000, Currency.getInstance("USD"));
        agreement1.setFinancedAmount(financedAmount1);
        agreement1.setAccount(account);
        devicePaymentAgreementRepository.saveAndFlush(agreement1);


        DevicePaymentAgreement agreement2 = new DevicePaymentAgreement();
        agreement2.setStartDate(startDate);
        agreement2.setEndDate(endDate);
        agreement2.setTotalInstallments(24);
        agreement2.setPaidInstallments(12);
        Money financedAmount2 = new Money(45000, Currency.getInstance("USD"));
        agreement2.setFinancedAmount(financedAmount2);
        agreement2.setAccount(account);
        devicePaymentAgreementRepository.saveAndFlush(agreement2);

        List<DevicePaymentAgreement> devicePaymentAgreementList = devicePaymentAgreementRepository.findByAccount(account);
        assertTrue(devicePaymentAgreementList.stream()
                .allMatch(dpa -> dpa.getAccount().equals(account)));
        assertEquals(3, devicePaymentAgreementList.size());
    }

    @Test
    void shouldThrowExceptionWhenStartDateIsNull() {

        DevicePaymentAgreement devicePaymentAgreement = new DevicePaymentAgreement();
        devicePaymentAgreement.setStartDate(null);
        devicePaymentAgreement.setEndDate(endDate);
        devicePaymentAgreement.setTotalInstallments(36);
        devicePaymentAgreement.setPaidInstallments(6);
        Money financedAmount = new Money(100000, Currency.getInstance("USD"));
        devicePaymentAgreement.setFinancedAmount(financedAmount);
        devicePaymentAgreement.setAccount(account);

        assertThrows(DataIntegrityViolationException.class, () -> devicePaymentAgreementRepository.saveAndFlush(devicePaymentAgreement));
    }

    @Test
    void shouldThrowExceptionWhenAccountIsNull() {

        DevicePaymentAgreement devicePaymentAgreement = new DevicePaymentAgreement();
        devicePaymentAgreement.setStartDate(startDate);
        devicePaymentAgreement.setEndDate(endDate);
        devicePaymentAgreement.setTotalInstallments(36);
        devicePaymentAgreement.setPaidInstallments(6);
        Money financedAmount = new Money(100000, Currency.getInstance("USD"));
        devicePaymentAgreement.setFinancedAmount(financedAmount);
        devicePaymentAgreement.setAccount(null);

        assertThrows(DataIntegrityViolationException.class, () -> devicePaymentAgreementRepository.saveAndFlush(devicePaymentAgreement));
    }
}
