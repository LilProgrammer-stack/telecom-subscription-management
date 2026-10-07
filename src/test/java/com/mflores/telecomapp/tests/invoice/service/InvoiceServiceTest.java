package com.mflores.telecomapp.tests.invoice.service;

import com.mflores.telecomapp.dto.InvoiceResponse;
import com.mflores.telecomapp.model.*;
import com.mflores.telecomapp.repository.InvoiceRepository;
import com.mflores.telecomapp.repository.PlanAssignmentRepository;
import com.mflores.telecomapp.service.InvoiceNumberGenerator;
import com.mflores.telecomapp.service.InvoiceService;
import static org.junit.jupiter.api.Assertions.*;

import com.mflores.telecomapp.service.PlanChargeCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private InvoiceNumberGenerator invoiceNumberGenerator;
    @Mock
    private PlanAssignmentRepository  planAssignmentRepository;
    @Mock
    private PlanChargeCalculator planChargeCalculator;
    @InjectMocks
    private InvoiceService invoiceService;

    @Test
    void shouldGenerateDueDateAndIssueDate() {

        BillingCycle  billingCycle = new BillingCycle();
        billingCycle.setPeriodStartDate(LocalDate.of(2020, 1, 1));

        Invoice invoice = new Invoice();
        invoice.setBillingCycle(billingCycle);
        invoice.setInvoiceNumber("INV100001");
        invoice.setIssueDate(LocalDate.of(2020, 1, 5));
        invoice.setDueDate(LocalDate.of(2020, 1, 21));
        invoice.setStatus(InvoiceStatus.ISSUED);

        when(invoiceNumberGenerator.generateInvoiceNumber())
                .thenReturn("INV100001");

        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        InvoiceResponse response = invoiceService.createInvoice(billingCycle);

        assertEquals(LocalDate.of(2020, 1, 1).plusDays(4), response.getIssueDate());
        assertEquals(LocalDate.of(2020, 1, 1).plusDays(20), response.getDueDate());

        verify(invoiceRepository).save(any(Invoice.class));
    }

    @Test
    void shouldGenerateInvoiceSuccessfully() {

        BillingCycle  billingCycle = new BillingCycle();
        billingCycle.setCycleNumber(1);
        billingCycle.setPeriodStartDate(LocalDate.of(2020, 1, 1));
        billingCycle.setPeriodEndDate(LocalDate.of(2020, 1, 31));

        Plan plan1 = new Plan();
        plan1.setPrice(new Money(10000, Currency.getInstance("USD")));
        plan1.setDescription("Exclusive Plan");
        PlanAssignment planAssignment1 = new PlanAssignment();
        planAssignment1.setStartDate(LocalDate.of(2020, 1, 1));
        planAssignment1.setEndDate(LocalDate.of(2020, 1, 15));
        planAssignment1.setPlan(plan1);

        Plan plan2 = new Plan();
        plan2.setPrice(new Money(7500, Currency.getInstance("USD")));
        plan2.setDescription("Intermediate plan");
        PlanAssignment planAssignment2 = new PlanAssignment();
        planAssignment2.setStartDate(LocalDate.of(2020, 1, 1));
        planAssignment2.setEndDate(LocalDate.of(2020, 1, 15));
        planAssignment2.setPlan(plan2);

        when(planAssignmentRepository.findAssignmentsForBillingCycle(billingCycle.getPeriodStartDate(), billingCycle.getPeriodEndDate()))
        .thenReturn(List.of(planAssignment1, planAssignment2));

        when(invoiceNumberGenerator.generateInvoiceNumber())
                .thenReturn("INV100001");

        when(planChargeCalculator.calculate(
                any(Money.class),
                any(LocalDate.class),
                any(LocalDate.class),
                any(LocalDate.class),
                any(LocalDate.class)
        )).thenReturn(new Money(5000, Currency.getInstance("USD")));

        when(invoiceRepository.save(any(Invoice.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        invoiceService.generateInvoice(billingCycle);

        ArgumentCaptor<Invoice> invoiceArgumentCaptor = ArgumentCaptor.forClass(Invoice.class);

        verify(invoiceRepository).save(invoiceArgumentCaptor.capture());

        Invoice invoice = invoiceArgumentCaptor.getValue();

        assertEquals(billingCycle,  invoice.getBillingCycle());
        assertEquals(InvoiceStatus.ISSUED, invoice.getStatus());
        assertEquals(2, invoice.getInvoiceLineItems().size());
        assertEquals("INV100001", invoice.getInvoiceNumber());

        assertEquals(5000, invoice.getInvoiceLineItems().get(0).getAmount().amountInCents());
        assertEquals(5000,  invoice.getInvoiceLineItems().get(1).getAmount().amountInCents());
        assertEquals(10000, invoice.getTotalAmount().amountInCents());
        assertEquals(Currency.getInstance("USD"), invoice.getTotalAmount().currency());

        assertEquals("Exclusive Plan", invoice.getInvoiceLineItems().get(0).getDescription());

        assertEquals("Intermediate plan", invoice.getInvoiceLineItems().get(1).getDescription());

        assertEquals(InvoiceLineItemType.PLAN, invoice.getInvoiceLineItems().get(0).getInvoiceLineItemType());

        assertEquals(InvoiceLineItemType.PLAN, invoice.getInvoiceLineItems().get(1).getInvoiceLineItemType());

        verify(planChargeCalculator, Mockito.times(2)).calculate(
                any(Money.class),
                any(LocalDate.class),
                any(LocalDate.class),
                any(LocalDate.class),
                any(LocalDate.class)
        );
    }

    @Test
    void shouldNotGenerateInvoiceWhenThereAreNoActivePlans() {

        BillingCycle billingCycle = new BillingCycle();
        billingCycle.setCycleNumber(1);
        billingCycle.setPeriodStartDate(LocalDate.of(2020, 1, 1));
        billingCycle.setPeriodEndDate(LocalDate.of(2020, 1, 31));

        when(planAssignmentRepository.findAssignmentsForBillingCycle(
                billingCycle.getPeriodStartDate(),
                billingCycle.getPeriodEndDate()
        )).thenReturn(List.of());

        assertThrows(
                IllegalStateException.class,
                () -> invoiceService.generateInvoice(billingCycle)
        );

        verify(invoiceRepository, never()).save(any(Invoice.class));
    }
}
