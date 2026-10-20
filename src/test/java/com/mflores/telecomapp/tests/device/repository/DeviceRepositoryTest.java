package com.mflores.telecomapp.tests.device.repository;

import com.mflores.telecomapp.model.*;
import com.mflores.telecomapp.repository.AccountOwnerRepository;
import com.mflores.telecomapp.repository.AccountRepository;
import com.mflores.telecomapp.repository.DevicePaymentAgreementRepository;
import com.mflores.telecomapp.repository.DeviceRepository;
import static org.junit.jupiter.api.Assertions.*;
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

public class DeviceRepositoryTest {

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DevicePaymentAgreementRepository devicePaymentAgreementRepository;
    @Autowired
    private AccountOwnerRepository accountOwnerRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldReturnAllLockedDevices() {

        AccountOwner accountOwner = new AccountOwner("Miguel", "Flores", "floresjosex50@gmail.com",
                LocalDate.of(2002, 11, 12));
        accountOwnerRepository.save(accountOwner);

        Account account = new Account("123", BillingLanguage.ENGLISH, AccountStatus.ACTIVE, OffsetDateTime.now(),
                accountOwner);
        accountRepository.save(account);

        DevicePaymentAgreement devicePaymentAgreement = new DevicePaymentAgreement();
        Money financedAmount = new Money(10000, Currency.getInstance("USD"));
        devicePaymentAgreement.setFinancedAmount(financedAmount);
        devicePaymentAgreement.setPaidInstallments(0);
        devicePaymentAgreement.setTotalInstallments(36);
        devicePaymentAgreement.setStartDate(LocalDate.of(2020, 1, 1));
        devicePaymentAgreement.setEndDate(devicePaymentAgreement.getStartDate().plusYears(3));
        devicePaymentAgreement.setAccount(account);
        devicePaymentAgreementRepository.saveAndFlush(devicePaymentAgreement);

        Device device = new Device();
        device.setBrand(BrandType.APPLE);
        device.setModel("AppleModel");
        device.setImei("123456789123456");
        Money price = new Money(10000, Currency.getInstance("USD"));
        device.setPrice(price);
        device.setPaymentAgreement(devicePaymentAgreement);
        device.setLocked(true);
        device.setFinancingEligible(true);
        deviceRepository.saveAndFlush(device);

        // --- Agreement 1: Started in 2021 (Completed) ---
        DevicePaymentAgreement agreement1 = new DevicePaymentAgreement();
        Money financedAmount1 = new Money(120000, Currency.getInstance("USD")); // $1,200.00
        agreement1.setFinancedAmount(financedAmount1);
        agreement1.setPaidInstallments(36);
        agreement1.setTotalInstallments(36);
        agreement1.setStartDate(LocalDate.of(2021, 5, 15));
        agreement1.setEndDate(agreement1.getStartDate().plusYears(3)); // May 15, 2024
        agreement1.setAccount(account);
        devicePaymentAgreementRepository.saveAndFlush(agreement1);

        Device device1 = new Device();
        device1.setBrand(BrandType.APPLE);
        device1.setModel("iPhone 13 Pro");
        device1.setImei("987654321098765");
        device1.setPrice(financedAmount1);
        device1.setPaymentAgreement(agreement1);
        device1.setLocked(false); // Fully paid, so unlocked
        device1.setFinancingEligible(false);
        deviceRepository.saveAndFlush(device1);


// --- Agreement 2: Started in 2025 (Active / Ongoing) ---
        DevicePaymentAgreement agreement2 = new DevicePaymentAgreement();
        Money financedAmount2 = new Money(99900, Currency.getInstance("USD")); // $999.00
        agreement2.setFinancedAmount(financedAmount2);
        agreement2.setPaidInstallments(12); // 1 year of payments completed
        agreement2.setTotalInstallments(36);
        agreement2.setStartDate(LocalDate.of(2025, 9, 1));
        agreement2.setEndDate(agreement2.getStartDate().plusYears(3)); // September 1, 2028
        agreement2.setAccount(account);
        devicePaymentAgreementRepository.saveAndFlush(agreement2);

        Device device2 = new Device();
        device2.setBrand(BrandType.SAMSUNG);
        device2.setModel("Galaxy S25");
        device2.setImei("567890123456789");
        device2.setPrice(financedAmount2);
        device2.setPaymentAgreement(agreement2);
        device2.setLocked(true); // Still under active finance
        device2.setFinancingEligible(false);
        deviceRepository.saveAndFlush(device2);

        List<Device> lockedDevices = deviceRepository.findByLocked(true);

        assertEquals(2, lockedDevices.size());
    }

    @Test
    void shouldThrowExceptionWhenBrandIsNull(){

        Device device = new Device();
        device.setBrand(null);
        device.setModel("AppleModel");
        device.setImei("123456789123456");
        Money price = new Money(10000, Currency.getInstance("USD"));
        device.setPrice(price);
        device.setLocked(false);

        assertThrows(DataIntegrityViolationException.class, () -> deviceRepository.saveAndFlush(device));
    }

    @Test
    void shouldThrowExceptionWhenModelIsNull(){

        Device device = new Device();
        device.setBrand(BrandType.APPLE);
        device.setModel(null);
        device.setImei("123456789123456");
        Money price = new Money(10000, Currency.getInstance("USD"));
        device.setPrice(price);
        device.setLocked(false);

        assertThrows(DataIntegrityViolationException.class, () -> deviceRepository.saveAndFlush(device));
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNull(){

        Device device = new Device();
        device.setBrand(BrandType.APPLE);
        device.setModel("AppleModel");
        device.setImei("123456789123456");
        device.setPrice(null);
        device.setLocked(false);

        assertThrows(DataIntegrityViolationException.class, () -> deviceRepository.saveAndFlush(device));
    }

    @Test
    void shouldThrowExceptionWhenLockedIsNull(){

        Device device = new Device();
        device.setBrand(BrandType.APPLE);
        device.setModel("AppleModel");
        device.setImei("123456789123456");
        Money price = new Money(10000, Currency.getInstance("USD"));
        device.setPrice(price);
        device.setLocked(null);

        assertThrows(DataIntegrityViolationException.class, () -> deviceRepository.saveAndFlush(device));
    }
}
