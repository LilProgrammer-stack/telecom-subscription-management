package com.mflores.telecomapp.tests.invoice.repository;

import com.mflores.telecomapp.model.*;
import com.mflores.telecomapp.repository.AccountOwnerRepository;
import com.mflores.telecomapp.repository.AccountRepository;
import com.mflores.telecomapp.repository.BillingCycleRepository;
import com.mflores.telecomapp.repository.InvoiceRepository;
import com.mflores.telecomapp.service.AccountNumberGenerator;
import com.mflores.telecomapp.service.InvoiceNumberGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.Currency;

@DataJpaTest
@Import({InvoiceNumberGenerator.class, AccountNumberGenerator.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private InvoiceNumberGenerator invoiceNumberGenerator;
    @Autowired
    private AccountOwnerRepository accountOwnerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BillingCycleRepository billingCycleRepository;
    @Autowired
    private AccountNumberGenerator accountNumberGenerator;

    @Test
    void shouldMapMoneyIntoTheDatabase() {

        AccountOwner accountOwner = new AccountOwner(
                "John",
                "Smith",
                "john.smith@example.com",
                LocalDate.of(1995, 4, 20)
        );
        accountOwnerRepository.save(accountOwner);

        Account account = new Account(
                accountNumberGenerator.generate(),
                BillingLanguage.ENGLISH,
                AccountStatus.ACTIVE,
                accountOwner
        );

        accountRepository.save(account);

        BillingCycle billingCycle1 = new BillingCycle(1, LocalDate.of(2026,5,11),
                LocalDate.of(2026,6,10), account);

        billingCycleRepository.saveAndFlush(billingCycle1);

        Invoice invoice = new Invoice();
        invoice.setBillingCycle(billingCycle1);
        invoice.setInvoiceNumber(invoiceNumberGenerator.generateInvoiceNumber());
        invoice.setStatus(InvoiceStatus.ISSUED);
        invoice.setIssueDate(LocalDate.of(2020, 1, 1));
        invoice.setDueDate(LocalDate.of(2020, 2, 5));
        Money money = new Money(100000, Currency.getInstance("USD"));
        invoice.setTotalAmount(money);

        Invoice savedInvoice = invoiceRepository.saveAndFlush(invoice);

        Assertions.assertEquals(100000, savedInvoice.getTotalAmount().amountInCents());
        Assertions.assertEquals(Currency.getInstance("USD"), savedInvoice.getTotalAmount().currency());
    }

    @Test
    void shouldThrowExceptionWhenInvoiceDoesNotHaveBillingCycle(){

        AccountOwner accountOwner = new AccountOwner(
                "John",
                "Smith",
                "john.smith@example.com",
                LocalDate.of(1995, 4, 20)
        );
        accountOwnerRepository.save(accountOwner);

        Account account = new Account(
                accountNumberGenerator.generate(),
                BillingLanguage.ENGLISH,
                AccountStatus.ACTIVE,
                accountOwner
        );

        accountRepository.save(account);

        Invoice invoice = new Invoice();
        invoice.setBillingCycle(null);
        invoice.setInvoiceNumber(invoiceNumberGenerator.generateInvoiceNumber());
        invoice.setStatus(InvoiceStatus.ISSUED);
        invoice.setIssueDate(LocalDate.of(2020, 1, 1));
        invoice.setDueDate(LocalDate.of(2020, 2, 5));
        Money money = new Money(100000, Currency.getInstance("USD"));
        invoice.setTotalAmount(money);



        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {

            invoiceRepository.saveAndFlush(invoice);
        });
    }

    @Test
    void shouldThrowExceptionWhenInvoiceDoesNotHaveInvoiceNumber(){

        AccountOwner accountOwner = new AccountOwner(
                "John",
                "Smith",
                "john.smith@example.com",
                LocalDate.of(1995, 4, 20)
        );
        accountOwnerRepository.save(accountOwner);

        Account account = new Account(
                accountNumberGenerator.generate(),
                BillingLanguage.ENGLISH,
                AccountStatus.ACTIVE,
                accountOwner
        );

        accountRepository.save(account);

        BillingCycle billingCycle1 = new BillingCycle(1, LocalDate.of(2026,5,11),
                LocalDate.of(2026,6,10), account);

        billingCycleRepository.saveAndFlush(billingCycle1);

        Invoice invoice = new Invoice();
        invoice.setBillingCycle(billingCycle1);
        invoice.setInvoiceNumber(null);
        invoice.setStatus(InvoiceStatus.ISSUED);
        invoice.setIssueDate(LocalDate.of(2020, 1, 1));
        invoice.setDueDate(LocalDate.of(2020, 2, 5));
        Money money = new Money(100000, Currency.getInstance("USD"));
        invoice.setTotalAmount(money);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            invoiceRepository.saveAndFlush(invoice);
        });
    }

    @Test
    void shouldThrowExceptionWhenInvoiceDoesNotHaveDueDate(){

        AccountOwner accountOwner = new AccountOwner(
                "John",
                "Smith",
                "john.smith@example.com",
                LocalDate.of(1995, 4, 20)
        );
        accountOwnerRepository.save(accountOwner);

        Account account = new Account(
                accountNumberGenerator.generate(),
                BillingLanguage.ENGLISH,
                AccountStatus.ACTIVE,
                accountOwner
        );

        accountRepository.save(account);

        BillingCycle billingCycle1 = new BillingCycle(1, LocalDate.of(2026,5,11),
                LocalDate.of(2026,6,10), account);

        billingCycleRepository.saveAndFlush(billingCycle1);

        Invoice invoice = new Invoice();
        invoice.setBillingCycle(billingCycle1);
        invoice.setInvoiceNumber(invoiceNumberGenerator.generateInvoiceNumber());
        invoice.setStatus(InvoiceStatus.ISSUED);
        invoice.setIssueDate(LocalDate.of(2020, 1, 1));
        invoice.setDueDate(null);
        Money money = new Money(100000, Currency.getInstance("USD"));
        invoice.setTotalAmount(money);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            invoiceRepository.saveAndFlush(invoice);
        });
    }

    @Test
    void shouldThrowExceptionWhenInvoiceDoesNotHaveStatus(){

        AccountOwner accountOwner = new AccountOwner(
                "John",
                "Smith",
                "john.smith@example.com",
                LocalDate.of(1995, 4, 20)
        );
        accountOwnerRepository.save(accountOwner);

        Account account = new Account(
                accountNumberGenerator.generate(),
                BillingLanguage.ENGLISH,
                AccountStatus.ACTIVE,
                accountOwner
        );

        accountRepository.save(account);

        BillingCycle billingCycle1 = new BillingCycle(1, LocalDate.of(2026,5,11),
                LocalDate.of(2026,6,10), account);

        billingCycleRepository.saveAndFlush(billingCycle1);

        Invoice invoice = new Invoice();
        invoice.setBillingCycle(billingCycle1);
        invoice.setInvoiceNumber(invoiceNumberGenerator.generateInvoiceNumber());
        invoice.setStatus(null);
        invoice.setIssueDate(LocalDate.of(2020, 1, 1));
        invoice.setDueDate(LocalDate.of(2020, 2, 5));
        Money money = new Money(100000, Currency.getInstance("USD"));
        invoice.setTotalAmount(money);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            invoiceRepository.saveAndFlush(invoice);
        });
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative(){

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Money money = new Money(-1000, Currency.getInstance("USD"));
        });
    }

    @Test
    void shouldNotAllowDuplicatedInvoiceNumber(){

        AccountOwner accountOwner = new AccountOwner(
                "John",
                "Smith",
                "john.smith@example.com",
                LocalDate.of(1995, 4, 20)
        );
        accountOwnerRepository.save(accountOwner);

        Account account = new Account(
                accountNumberGenerator.generate(),
                BillingLanguage.ENGLISH,
                AccountStatus.ACTIVE,
                accountOwner
        );

        accountRepository.save(account);

        BillingCycle billingCycle1 = new BillingCycle(1, LocalDate.of(2026,5,11),
                LocalDate.of(2026,6,10), account);

        BillingCycle billingCycle2 = new BillingCycle(2, LocalDate.of(2026,6,11),
                LocalDate.of(2026,7,10), account);

        billingCycleRepository.saveAndFlush(billingCycle1);
        billingCycleRepository.saveAndFlush(billingCycle2);

        Invoice invoice = new Invoice();
        invoice.setBillingCycle(billingCycle1);
        String invoiceNumber = invoiceNumberGenerator.generateInvoiceNumber();
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setStatus(InvoiceStatus.ISSUED);
        invoice.setIssueDate(LocalDate.of(2020, 1, 1));
        invoice.setDueDate(LocalDate.of(2020, 2, 5));
        Money money = new Money(100000, Currency.getInstance("USD"));
        invoice.setTotalAmount(money);
        invoiceRepository.save(invoice);

        Invoice invoice2 = new Invoice();
        invoice2.setBillingCycle(billingCycle2);
        invoice2.setInvoiceNumber(invoiceNumber);
        invoice2.setStatus(InvoiceStatus.ISSUED);
        invoice2.setIssueDate(LocalDate.of(2020, 1, 1));
        invoice2.setDueDate(LocalDate.of(2020, 2, 5));
        Money money2 = new Money(100000, Currency.getInstance("USD"));
        invoice2.setTotalAmount(money2);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            invoiceRepository.save(invoice2);
        });
    }

    @Test
    void shouldNotAllowASecondInvoiceForOneBillingCycle(){

        AccountOwner accountOwner = new AccountOwner(
                "John",
                "Smith",
                "john.smith@example.com",
                LocalDate.of(1995, 4, 20)
        );
        accountOwnerRepository.save(accountOwner);

        Account account = new Account(
                accountNumberGenerator.generate(),
                BillingLanguage.ENGLISH,
                AccountStatus.ACTIVE,
                accountOwner
        );

        accountRepository.save(account);

        BillingCycle billingCycle1 = new BillingCycle(1, LocalDate.of(2026,5,11),
                LocalDate.of(2026,6,10), account);

        BillingCycle billingCycle2 = new BillingCycle(2, LocalDate.of(2026,6,11),
                LocalDate.of(2026,7,10), account);

        billingCycleRepository.saveAndFlush(billingCycle1);
        billingCycleRepository.saveAndFlush(billingCycle2);

        Invoice invoice = new Invoice();
        invoice.setBillingCycle(billingCycle1);
        invoice.setInvoiceNumber(invoiceNumberGenerator.generateInvoiceNumber());
        invoice.setStatus(InvoiceStatus.ISSUED);
        invoice.setIssueDate(LocalDate.of(2020, 1, 1));
        invoice.setDueDate(LocalDate.of(2020, 2, 5));
        Money money = new Money(100000, Currency.getInstance("USD"));
        invoice.setTotalAmount(money);
        invoiceRepository.save(invoice);

        Invoice invoice2 = new Invoice();
        invoice2.setBillingCycle(billingCycle1);
        invoice2.setInvoiceNumber(invoiceNumberGenerator.generateInvoiceNumber());
        invoice2.setStatus(InvoiceStatus.ISSUED);
        invoice2.setIssueDate(LocalDate.of(2020, 1, 1));
        invoice2.setDueDate(LocalDate.of(2020, 2, 5));
        Money money2 = new Money(100000, Currency.getInstance("USD"));
        invoice2.setTotalAmount(money2);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            invoiceRepository.save(invoice2);
        });
    }
}
