package com.mflores.telecomapp.service;

import com.mflores.telecomapp.dto.InvoiceResponse;
import com.mflores.telecomapp.model.*;
import com.mflores.telecomapp.repository.InvoiceRepository;
import com.mflores.telecomapp.repository.PlanAssignmentRepository;
import com.mflores.telecomapp.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    private InvoiceRepository invoiceRepository;
    private InvoiceNumberGenerator invoiceNumberGenerator;
    private PlanAssignmentRepository planAssignmentRepository;
    private PlanChargeCalculator  planChargeCalculator;

    public InvoiceService(InvoiceRepository invoiceRepository, InvoiceNumberGenerator invoiceNumberGenerator,
                          PlanAssignmentRepository planRepository, PlanChargeCalculator planChargeCalculator) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceNumberGenerator = invoiceNumberGenerator;
        this.planAssignmentRepository = planRepository;
        this.planChargeCalculator = planChargeCalculator;
    }

    public InvoiceResponse createInvoice(BillingCycle billingCycle){

        InvoiceDates dates = calculateInvoiceDates(billingCycle);
        Invoice invoice = new Invoice();
        invoice.setBillingCycle(billingCycle);
        invoice.setInvoiceNumber(invoiceNumberGenerator.generateInvoiceNumber());
        invoice.setStatus(InvoiceStatus.ISSUED);
        invoice.setIssueDate(dates.issueDate());
        invoice.setDueDate(dates.dueDate());
        //invoice.setTotalAmount();

        Invoice savedInvoice = invoiceRepository.save(invoice);

        return convertInInvoiceResponse(savedInvoice);
    }

    private InvoiceDates calculateInvoiceDates(BillingCycle billingCycle){

        LocalDate issueDate = billingCycle.getPeriodStartDate().plusDays(4);
        LocalDate dueDate = billingCycle.getPeriodStartDate().plusDays(20);

        return new InvoiceDates(issueDate, dueDate);
    }

    public InvoiceResponse generateInvoice(BillingCycle billingCycle){

        List<PlanAssignment> planAssignments = planAssignmentRepository
                .findAssignmentsForBillingCycle(billingCycle.getPeriodStartDate(), billingCycle.getPeriodEndDate());

        if (planAssignments.isEmpty()){
            throw new IllegalStateException("No billable plan assignments found, could not generate invoice");
        }

        Invoice invoice = new Invoice();
        InvoiceDates dates = calculateInvoiceDates(billingCycle);

        invoice.setBillingCycle(billingCycle);
        invoice.setInvoiceNumber(invoiceNumberGenerator.generateInvoiceNumber());
        invoice.setStatus(InvoiceStatus.ISSUED);
        invoice.setIssueDate(dates.issueDate());
        invoice.setDueDate(dates.dueDate());

        List<InvoiceLineItem> invoiceLinePlans = planAssignments.stream()
                .map(planAssignment -> {
                    Money charge = planChargeCalculator.calculate(planAssignment.getPlan().getPrice(),
                            billingCycle.getPeriodStartDate(), billingCycle.getPeriodEndDate(),
                            planAssignment.getStartDate(), planAssignment.getEndDate());

                    InvoiceLineItem invoiceLineItem = new InvoiceLineItem();
                    invoiceLineItem.setDescription(planAssignment.getPlan().getDescription());
                    invoiceLineItem.setAmount(charge);
                    invoiceLineItem.setInvoiceLineItemType(InvoiceLineItemType.PLAN);
                    invoiceLineItem.setInvoice(invoice);

                    return invoiceLineItem;
                }).toList();

        Money total = invoiceLinePlans.stream()
                .map(InvoiceLineItem::getAmount)
                .reduce(Money::add)
                .orElse(new Money(0, Currency.getInstance("USD")));

        invoice.setTotalAmount(total);
        invoice.setInvoiceLineItems(invoiceLinePlans);

        Invoice savedInvoice = invoiceRepository.save(invoice);

        return convertInInvoiceResponse(savedInvoice);
    }

    private InvoiceResponse convertInInvoiceResponse(Invoice invoice){
        return new InvoiceResponse(invoice.getInvoiceId(), invoice.getInvoiceNumber(),
                invoice.getTotalAmount(), invoice.getIssueDate(), invoice.getDueDate(),
                invoice.getStatus(), invoice.getBillingCycle().getBillingCycleId());
    }
}
