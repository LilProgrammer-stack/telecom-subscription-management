package com.mflores.telecomapp.tests.invoicelineitem.service;

import com.mflores.telecomapp.model.*;
import com.mflores.telecomapp.repository.*;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Currency;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class InvoiceLineItemRepositoryTest {

    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private InvoiceLineItemRepository invoiceLineItemRepository;
    @Autowired
    private AccountOwnerRepository  accountOwnerRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BillingCycleRepository billingCycleRepository;

    @Autowired
    private EntityManager entityManager;

    AccountOwner accountOwner = new AccountOwner();
    Account account = new Account();
    BillingCycle billingCycle = new BillingCycle();

    @BeforeEach
    public void setup() {

        accountOwner.setFirstName("Miguel");
        accountOwner.setLastName("Flores");
        accountOwner.setEmail("floresjosex50@gmail.com");
        accountOwner.setCreatedAt(OffsetDateTime.now());
        accountOwner.setDateOfBirth(LocalDate.of(2002, 11, 12));
        accountOwnerRepository.saveAndFlush(accountOwner);


        account.setAccountOwner(accountOwner);
        account.setBillingLanguage(BillingLanguage.ENGLISH);
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setAccountNumber("TB123456");
        account.setCreationDate(OffsetDateTime.now().plusDays(1));
        accountRepository.saveAndFlush(account);


        billingCycle.setCycleNumber(1);
        billingCycle.setAccount(account);
        billingCycle.setPeriodStartDate(LocalDate.of(2002, 11, 12));
        billingCycle.setPeriodEndDate(LocalDate.of(2002, 12, 11));
        billingCycleRepository.saveAndFlush(billingCycle);

    }

    @Test
    void shouldEstablishRelationshipBetweenInvoiceAndInvoiceLineItem() {

        Invoice invoice = new Invoice();

        invoice.setInvoiceNumber("INV123456");
        invoice.setStatus(InvoiceStatus.ISSUED);
        invoice.setDueDate(LocalDate.of(2020, 1, 21));
        invoice.setIssueDate(LocalDate.of(2020, 1, 1));
        Money total = new Money(10000, Currency.getInstance("USD"));
        invoice.setTotalAmount(total);
        invoice.setBillingCycle(billingCycle);
        Invoice savedInvoice = invoiceRepository.saveAndFlush(invoice);


        InvoiceLineItem invoiceLineItem1 = new InvoiceLineItem();
        invoiceLineItem1.setInvoiceLineItemType(InvoiceLineItemType.PLAN);
        invoiceLineItem1.setInvoice(invoice);
        invoiceLineItem1.setDescription("Plan Description");
        Money money1 = new Money(10000,  Currency.getInstance("USD"));
        invoiceLineItem1.setAmount(money1);
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem1);
        invoice.getInvoiceLineItems().add(invoiceLineItem1);


        InvoiceLineItem invoiceLineItem2 = new InvoiceLineItem();
        invoiceLineItem2.setInvoiceLineItemType(InvoiceLineItemType.PLAN);
        invoiceLineItem2.setInvoice(invoice);
        invoiceLineItem2.setDescription("Plan Description");
        Money money2 = new Money(10000,  Currency.getInstance("USD"));
        invoiceLineItem2.setAmount(money2);
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem2);
        invoice.getInvoiceLineItems().add(invoiceLineItem2);


        InvoiceLineItem invoiceLineItem3 = new InvoiceLineItem();
        invoiceLineItem3.setInvoiceLineItemType(InvoiceLineItemType.PLAN);
        invoiceLineItem3.setInvoice(invoice);
        invoiceLineItem3.setDescription("Plan Description");
        Money money3 = new Money(10000,  Currency.getInstance("USD"));
        invoiceLineItem3.setAmount(money3);
        invoiceLineItemRepository.saveAndFlush(invoiceLineItem3);
        invoice.getInvoiceLineItems().add(invoiceLineItem3);

        entityManager.flush();
        entityManager.clear();

        Invoice reloadedInvoice = invoiceRepository.findById(savedInvoice.getInvoiceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        assertEquals(3, reloadedInvoice.getInvoiceLineItems().size());

        reloadedInvoice.getInvoiceLineItems()
                .forEach(invoiceLineItem -> {
                    assertEquals(reloadedInvoice.getInvoiceId(), invoiceLineItem.getInvoice().getInvoiceId());
                });

    }

    @Test
    void shouldNotAllowNullItemType() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV123456");
        invoice.setStatus(InvoiceStatus.ISSUED);
        invoice.setDueDate(LocalDate.of(2020, 1, 21));
        invoice.setIssueDate(LocalDate.of(2020, 1, 1));
        Money total = new Money(10000, Currency.getInstance("USD"));
        invoice.setTotalAmount(total);
        invoice.setBillingCycle(billingCycle);
        invoiceRepository.saveAndFlush(invoice);

        InvoiceLineItem invoiceLineItem = new InvoiceLineItem();
        invoiceLineItem.setInvoiceLineItemType(null);
        invoiceLineItem.setInvoice(invoice);
        invoiceLineItem.setDescription("Plan Description");
        Money money = new Money(10000,  Currency.getInstance("USD"));
        invoiceLineItem.setAmount(money);

        assertThrows(DataIntegrityViolationException.class, () -> invoiceLineItemRepository.saveAndFlush(invoiceLineItem));
    }

    @Test
    void shouldNotAllowNullDescription() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV123456");
        invoice.setStatus(InvoiceStatus.ISSUED);
        invoice.setDueDate(LocalDate.of(2020, 1, 21));
        invoice.setIssueDate(LocalDate.of(2020, 1, 1));
        Money total = new Money(10000, Currency.getInstance("USD"));
        invoice.setTotalAmount(total);
        invoice.setBillingCycle(billingCycle);
        invoiceRepository.saveAndFlush(invoice);

        InvoiceLineItem invoiceLineItem = new InvoiceLineItem();
        invoiceLineItem.setInvoiceLineItemType(InvoiceLineItemType.PLAN);
        invoiceLineItem.setInvoice(invoice);
        invoiceLineItem.setDescription(null);
        Money money = new Money(10000,  Currency.getInstance("USD"));
        invoiceLineItem.setAmount(money);

        assertThrows(DataIntegrityViolationException.class, () -> invoiceLineItemRepository.saveAndFlush(invoiceLineItem));
    }

    @Test
    void shouldNotAllowNullAmount() {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV123456");
        invoice.setStatus(InvoiceStatus.ISSUED);
        invoice.setDueDate(LocalDate.of(2020, 1, 21));
        invoice.setIssueDate(LocalDate.of(2020, 1, 1));
        Money total = new Money(10000, Currency.getInstance("USD"));
        invoice.setTotalAmount(total);
        invoice.setBillingCycle(billingCycle);
        invoiceRepository.saveAndFlush(invoice);

        InvoiceLineItem invoiceLineItem = new InvoiceLineItem();
        invoiceLineItem.setInvoiceLineItemType(InvoiceLineItemType.PLAN);
        invoiceLineItem.setInvoice(invoice);
        invoiceLineItem.setDescription("Plan Description");
        invoiceLineItem.setAmount(null);

        assertThrows(DataIntegrityViolationException.class, () -> invoiceLineItemRepository.saveAndFlush(invoiceLineItem));
    }

    @Test
    void shouldNotAllowLineItemWithoutInvoice() {

        InvoiceLineItem invoiceLineItem = new InvoiceLineItem();
        invoiceLineItem.setInvoiceLineItemType(InvoiceLineItemType.PLAN);
        invoiceLineItem.setInvoice(null);
        invoiceLineItem.setDescription("Plan Description");
        Money money = new Money(10000,  Currency.getInstance("USD"));
        invoiceLineItem.setAmount(money);

        assertThrows(DataIntegrityViolationException.class, () -> invoiceLineItemRepository.saveAndFlush(invoiceLineItem));
    }


}
