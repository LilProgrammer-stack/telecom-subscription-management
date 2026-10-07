package com.mflores.telecomapp.repository;

import com.mflores.telecomapp.model.InvoiceLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceLineItemRepository extends JpaRepository<InvoiceLineItem, Long> {
}
