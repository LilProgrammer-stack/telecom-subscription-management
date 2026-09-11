package com.mflores.telecomapp.service;

import com.mflores.telecomapp.dto.InvoiceResponse;
import com.mflores.telecomapp.model.*;
import com.mflores.telecomapp.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class InvoiceService {

    private InvoiceRepository invoiceRepository;
    private InvoiceNumberGenerator invoiceNumberGenerator;

    public InvoiceService(InvoiceRepository invoiceRepository, InvoiceNumberGenerator invoiceNumberGenerator) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceNumberGenerator = invoiceNumberGenerator;
    }

    public InvoiceResponse createInvoice(BillingCycle billingCycle, Account account, AccountOwner accountOwner){

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

    private InvoiceResponse convertInInvoiceResponse(Invoice invoice){
        return new InvoiceResponse(invoice.getInvoiceId(), invoice.getInvoiceNumber(),
                invoice.getTotalAmount(), invoice.getIssueDate(), invoice.getDueDate(),
                invoice.getStatus(), invoice.getBillingCycle().getBillingCycleId());
    }
}
